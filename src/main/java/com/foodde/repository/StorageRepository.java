package com.foodde.repository;

import com.foodde.model.Order;
import com.foodde.model.Restaurant;
import com.foodde.model.User;
import java.util.List;

public interface StorageRepository {
    // Restaurant Operations
    void saveRestaurant(Restaurant restaurant);
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(String id);
    
    // User Operations
    void saveUser(User user);
    User getUserById(String id);
    
    // Order Operations
    void saveOrder(Order order);
    List<Order> getOrdersByUserId(String userId);
}
