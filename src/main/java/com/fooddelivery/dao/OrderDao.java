package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public int createOrder(int customerId, int restaurantId, double totalPrice, String status) throws Exception {
        String sql = "INSERT INTO orders(customer_id, restaurant_id, total_price, status) VALUES(?,?,?,?) RETURNING id";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, restaurantId);
            ps.setDouble(3, totalPrice);
            ps.setString(4, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        }
        return -1;
    }

    public void updateStatus(int orderId, String status) throws Exception {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public Order getOrderById(int orderId) throws Exception {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        }
        return null;
    }

    public List<Order> getOrdersByCustomer(int customerId) throws Exception {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id=? ORDER BY id DESC";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    public List<Order> getOrdersByRestaurant(int restaurantId) throws Exception {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE restaurant_id=? ORDER BY id DESC";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    private Order map(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("restaurant_id"),
                rs.getDouble("total_price"),
                rs.getString("status"));
    }
}