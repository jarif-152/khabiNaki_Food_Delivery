package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao {

    public void addOrderItem(int orderId, int menuItemId, int quantity) throws Exception {
        String sql = "INSERT INTO order_items(order_id, menu_item_id, quantity) VALUES(?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, menuItemId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public List<OrderItem> getOrderItems(int orderId) throws Exception {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new OrderItem(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("menu_item_id"),
                        rs.getInt("quantity")));
            }
        }
        return list;
    }
}