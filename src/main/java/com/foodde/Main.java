package com.foodde;

import com.foodde.model.Restaurant;
import com.foodde.repository.StorageRepository;
import com.foodde.repository.XmlRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        StorageRepository repository = new XmlRepository();

        // Initialize some sample data if the repository is empty
        if (repository.getAllRestaurants().isEmpty()) {
            Restaurant res1 = new Restaurant("1", "Burger King", "123 Main St", "9:00 AM - 10:00 PM");
            res1.getMenu().add(new com.foodde.model.MenuItem("m1", "Whopper", 5.99, "Flame-grilled beef patty"));
            res1.getMenu().add(new com.foodde.model.MenuItem("m2", "Chicken Fries", 4.49, "9pc Crispy chicken fries"));
            repository.saveRestaurant(res1);

            Restaurant res2 = new Restaurant("2", "Pizza Hut", "456 Oak Ave", "10:00 AM - 11:00 PM");
            res2.getMenu().add(new com.foodde.model.MenuItem("m3", "Pepperoni Pizza", 12.99, "Classic pepperoni pizza"));
            res2.getMenu().add(new com.foodde.model.MenuItem("m4", "Cheesy Garlic Bread", 5.50, "Freshly baked garlic bread"));
            repository.saveRestaurant(res2);
        } else {
            // Ensure restaurants have menus if they were created empty previously
            for (Restaurant res : repository.getAllRestaurants()) {
                if (res.getMenu().isEmpty()) {
                    if (res.getName().equals("Burger King")) {
                        res.getMenu().add(new com.foodde.model.MenuItem("m1", "Whopper", 5.99, "Flame-grilled beef patty"));
                        res.getMenu().add(new com.foodde.model.MenuItem("m2", "Chicken Fries", 4.49, "9pc Crispy chicken fries"));
                    } else if (res.getName().equals("Pizza Hut")) {
                        res.getMenu().add(new com.foodde.model.MenuItem("m3", "Pepperoni Pizza", 12.99, "Classic pepperoni pizza"));
                        res.getMenu().add(new com.foodde.model.MenuItem("m4", "Cheesy Garlic Bread", 5.50, "Freshly baked garlic bread"));
                    }
                    repository.saveRestaurant(res);
                }
            }
        }

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/static");
            config.fileRenderer(new JavalinThymeleaf());
        }).start(7070);

        // Controllers
        var restaurantController = new com.foodde.controller.RestaurantController(repository);
        var cartController = new com.foodde.controller.CartController(repository);
        var userController = new com.foodde.controller.UserController();
        var adminController = new com.foodde.controller.AdminController(repository);

        // Home Page Route
        app.get("/", restaurantController::listAll);
        
        // Restaurant Detail Route
        app.get("/restaurant/{id}", restaurantController::detail);

        // Cart Routes
        app.post("/cart/add", cartController::addToCart);
        app.get("/cart", cartController::viewCart);

        // User & Checkout Routes
        app.get("/login", userController::showLogin);
        app.post("/login", userController::login);
        app.post("/checkout", cartController::placeOrder);

        // Admin Routes
        app.get("/admin", adminController::dashboard);
        app.post("/admin/restaurant/add", adminController::addRestaurant);
        app.post("/admin/menu/add", adminController::addMenuItem);

        // SOAP / WSDL API Requirement
        com.foodde.service.RestaurantSoapService.setRepository(repository);
        jakarta.xml.ws.Endpoint.publish("http://localhost:8081/services/restaurant", new com.foodde.service.RestaurantSoapService());

        System.out.println("Server started at http://localhost:7070");
        System.out.println("WSDL API available at http://localhost:8081/services/restaurant?wsdl");
    }
}
