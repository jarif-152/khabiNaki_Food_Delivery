package com.foodde.controller;

import com.foodde.model.MenuItem;
import com.foodde.model.Restaurant;
import com.foodde.repository.StorageRepository;
import io.javalin.http.Context;
import java.util.Map;
import java.util.UUID;

public class AdminController {
    private final StorageRepository repository;

    public AdminController(StorageRepository repository) {
        this.repository = repository;
    }

    public void dashboard(Context ctx) {
        var restaurants = repository.getAllRestaurants();
        ctx.render("templates/admin/dashboard.html", Map.of("restaurants", restaurants));
    }

    public void addRestaurant(Context ctx) {
        String name = ctx.formParam("name");
        String address = ctx.formParam("address");
        String schedule = ctx.formParam("schedule");

        String id = UUID.randomUUID().toString().substring(0, 8);
        Restaurant res = new Restaurant(id, name, address, schedule);
        repository.saveRestaurant(res);

        ctx.redirect("/admin");
    }

    public void addMenuItem(Context ctx) {
        String restaurantId = ctx.formParam("restaurantId");
        String name = ctx.formParam("name");
        double price = Double.parseDouble(ctx.formParam("price"));
        String desc = ctx.formParam("description");

        var restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant != null) {
            String itemId = "m" + (restaurant.getMenu().size() + 1);
            restaurant.getMenu().add(new MenuItem(itemId, name, price, desc));
            repository.saveRestaurant(restaurant);
        }

        ctx.redirect("/admin");
    }
}
