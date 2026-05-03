package com.fooddelivery.service;

import com.fooddelivery.dao.MenuItemDao;
import com.fooddelivery.model.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

public class MenuService {
    private final MenuItemDao dao = new MenuItemDao();

    public List<MenuItem> getAvailableMenu(int restaurantId) throws Exception {
        return dao.getMenuByRestaurant(restaurantId)
                .stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }
}