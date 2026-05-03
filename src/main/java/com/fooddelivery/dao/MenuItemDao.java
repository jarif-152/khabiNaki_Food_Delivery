package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDao {

    public void addMenuItem(int restaurantId, String name, double price, int quantity, String addons) throws Exception {
        String sql = "INSERT INTO menu_items(restaurant_id, name, price, quantity, addons) VALUES(?,?,?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);
            ps.setString(5, addons);
            ps.executeUpdate();
        }
    }

    public List<MenuItem> getMenuByRestaurant(int restaurantId) throws Exception {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    public void updateAvailability(int menuItemId, boolean available) throws Exception {
        String sql = "UPDATE menu_items SET available=? WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();
        }
    }

    public void updateQuantity(int menuItemId, int quantity) throws Exception {
        String sql = "UPDATE menu_items SET quantity=? WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();
        }
    }

    public void updateAddons(int menuItemId, String addons) throws Exception {
        String sql = "UPDATE menu_items SET addons=? WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, addons);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();
        }
    }

    public void reduceQuantity(int menuItemId, int quantity) throws Exception {
        String sql = "UPDATE menu_items SET quantity = quantity - ? WHERE id = ?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();
        }
    }

    public void increaseQuantity(int menuItemId, int quantity) throws Exception {
        String sql = "UPDATE menu_items SET quantity = quantity + ? WHERE id = ?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();
        }
    }

    public void deleteMenuItem(int menuItemId) throws Exception {
        String sql = "DELETE FROM menu_items WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, menuItemId);
            ps.executeUpdate();
        }
    }

    private MenuItem map(ResultSet rs) throws SQLException {
        return new MenuItem(
                rs.getInt("id"),
                rs.getInt("restaurant_id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getBoolean("available"),
                rs.getInt("quantity"),
                rs.getString("addons"));
    }
}