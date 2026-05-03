package com.fooddelivery.service;

import com.fooddelivery.dao.RestaurantDao;
import com.fooddelivery.model.Restaurant;

import java.util.Comparator;
import java.util.List;

public class RestaurantService {
    private final RestaurantDao dao = new RestaurantDao();

    public List<Restaurant> getAllRestaurantsSorted() throws Exception {
        List<Restaurant> list = dao.getAllRestaurants();
        list.sort(Comparator.comparing(Restaurant::getName));
        return list;
    }

    public List<Restaurant> findByAreaSorted(String area) throws Exception {
        List<Restaurant> list = dao.searchByArea(area);
        list.sort(Comparator.comparing(Restaurant::getName));
        return list;
    }
}