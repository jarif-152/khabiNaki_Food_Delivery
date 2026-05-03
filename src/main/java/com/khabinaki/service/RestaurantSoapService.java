package com.khabinaki.service;

import com.khabinaki.model.MenuItem;
import com.khabinaki.model.Restaurant;
import com.khabinaki.repository.StorageRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebService(serviceName = "RestaurantService")
public class RestaurantSoapService {
    private static StorageRepository repository;

    public static void setRepository(StorageRepository repo) {
        repository = repo;
    }

    @WebMethod(operationName = "getRestaurantsByArea")
    public List<Restaurant> getRestaurantsByArea(@WebParam(name = "areaName") String areaName) {
        List<Restaurant> all = repository.getAllRestaurants();

        // Filter by address area
        return all.stream()
                .filter(r -> r.getAddress().toLowerCase().contains(areaName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @WebMethod(operationName = "getAllRestaurants")
    public List<Restaurant> getAllRestaurants() {
        return repository.getAllRestaurants();
    }

    @WebMethod(operationName = "getRestaurantById")
    public Restaurant getRestaurantById(@WebParam(name = "id") String id) {
        return repository.getRestaurantById(id);
    }

    // Get menu items for a specific restaurant
    @WebMethod(operationName = "getMenuByRestaurantId")
    public List<MenuItem> getMenuByRestaurantId(@WebParam(name = "restaurantId") String restaurantId) {
        Restaurant restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant != null) {
            return restaurant.getMenu();
        }
        return new ArrayList<>();
    }

    // Get only available menu items from a restaurant
    @WebMethod(operationName = "getAvailableMenuItems")
    public List<MenuItem> getAvailableMenuItems(@WebParam(name = "restaurantId") String restaurantId) {
        Restaurant restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant != null) {
            return restaurant.getMenu().stream()
                    .filter(MenuItem::isAvailable)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    // Search restaurants by name
    @WebMethod(operationName = "searchRestaurantsByName")
    public List<Restaurant> searchRestaurantsByName(@WebParam(name = "restaurantName") String restaurantName) {
        return repository.getAllRestaurants().stream()
                .filter(r -> r.getName().toLowerCase().contains(restaurantName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Get menu item count for restaurant
    @WebMethod(operationName = "getMenuItemCount")
    public int getMenuItemCount(@WebParam(name = "restaurantId") String restaurantId) {
        Restaurant restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant != null) {
            return restaurant.getMenu().size();
        }
        return 0;
    }

    // Check if restaurant is open (based on schedule string)
    @WebMethod(operationName = "getRestaurantSchedule")
    public String getRestaurantSchedule(@WebParam(name = "restaurantId") String restaurantId) {
        Restaurant restaurant = repository.getRestaurantById(restaurantId);
        if (restaurant != null) {
            return restaurant.getSchedule();
        }
        return "Restaurant not found";
    }

    // Get all restaurants with availability info
    @WebMethod(operationName = "getRestaurantsWithAvailability")
    public List<String> getRestaurantsWithAvailability() {
        List<String> result = new ArrayList<>();
        for (Restaurant r : repository.getAllRestaurants()) {
            long availableItems = r.getMenu().stream().filter(MenuItem::isAvailable).count();
            result.add(String.format("%s (%s) - %d items available | Hours: %s",
                    r.getId(), r.getName(), availableItems, r.getSchedule()));
        }
        return result;
    }
}