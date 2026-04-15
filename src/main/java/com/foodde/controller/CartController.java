package com.foodde.controller;

import com.foodde.model.Order;
import com.foodde.model.OrderItem;
import com.foodde.model.MenuItem;
import com.foodde.repository.StorageRepository;
import io.javalin.http.Context;
import java.util.Map;
import java.util.UUID;

public class CartController {
    private final StorageRepository repository;

    public CartController(StorageRepository repository) {
        this.repository = repository;
    }

    // Get or create session order
    private Order getSessionOrder(Context ctx) {
        Order order = ctx.sessionAttribute("cart");
        if (order == null) {
            // Temporary ID/User placeholder
            order = new Order(UUID.randomUUID().toString(), "guest", null);
            ctx.sessionAttribute("cart", order);
        }
        return order;
    }

    public void addToCart(Context ctx) {
        String restaurantId = ctx.formParam("restaurantId");
        String menuItemId = ctx.formParam("menuItemId");

        var restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant == null) {
            ctx.status(404).result("Restaurant not found");
            return;
        }

        // Find menu item
        MenuItem item = restaurant.getMenu().stream()
                .filter(m -> m.getId().equals(menuItemId))
                .findFirst()
                .orElse(null);

        if (item != null) {
            Order order = getSessionOrder(ctx);
            
            // Clear cart if items from different restaurant
            if (order.getRestaurantId() != null && !order.getRestaurantId().equals(restaurantId)) {
                order.getItems().clear();
            }
            
            order.setRestaurantId(restaurantId);
            
            // Add or increment quantity
            OrderItem existing = order.getItems().stream()
                    .filter(oi -> oi.getMenuItem().getId().equals(menuItemId))
                    .findFirst()
                    .orElse(null);
            
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + 1);
            } else {
                order.getItems().add(new OrderItem(item, 1));
            }
            
            order.calculateTotal();
        }

        // Redirect back to restaurant page
        ctx.redirect("/restaurant/" + restaurantId);
    }

    public void viewCart(Context ctx) {
        Order order = getSessionOrder(ctx);
        ctx.render("templates/cart.html", Map.of("order", order));
    }

    // Process checkout
    public void placeOrder(Context ctx) {
        com.foodde.model.User user = ctx.sessionAttribute("user");
        
        // Redirect to login if not logged in
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        Order order = getSessionOrder(ctx);
        if (order.getItems().isEmpty()) {
            ctx.redirect("/");
            return;
        }

        // Finalize order
        order.setUserId(user.getId());
        order.setStatus("Preparing");
        repository.saveOrder(order);

        // Clear cart session
        ctx.sessionAttribute("cart", null);

        ctx.render("templates/order-success.html", Map.of("order", order));
    }
}
