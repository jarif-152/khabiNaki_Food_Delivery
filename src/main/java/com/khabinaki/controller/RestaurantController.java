package com.khabinaki.controller;

import com.khabinaki.repository.StorageRepository;
import io.javalin.http.Context;
import java.util.Map;
import java.util.stream.Collectors;

public class RestaurantController {
    private final StorageRepository repository;

    public RestaurantController(StorageRepository repository) {
        this.repository = repository;
    }

    public void listAll(Context ctx) {
        String searchQuery = ctx.queryParam("search");
        var restaurants = repository.getAllRestaurants();
        
        // Filter by restaurant name or address if search query provided
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String query = searchQuery.toLowerCase();
            restaurants = restaurants.stream()
                    .filter(r -> r.getName().toLowerCase().contains(query) ||
                               r.getAddress().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }
        
        ctx.render("templates/index.html", Map.of(
                "restaurants", restaurants,
                "searchQuery", searchQuery != null ? searchQuery : ""
        ));
    }

    public void detail(Context ctx) {
        String id = ctx.pathParam("id");
        var restaurant = repository.getRestaurantById(id);
        
        if (restaurant == null) {
            ctx.status(404).result("Restaurant not found");
            return;
        }

        ctx.render("templates/restaurant-detail.html", Map.of("restaurant", restaurant));
    }
}
