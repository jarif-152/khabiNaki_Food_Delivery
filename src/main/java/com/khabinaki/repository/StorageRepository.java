package com.khabinaki.repository;

import com.khabinaki.model.Order;
import com.khabinaki.model.Restaurant;
import com.khabinaki.model.User;
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
