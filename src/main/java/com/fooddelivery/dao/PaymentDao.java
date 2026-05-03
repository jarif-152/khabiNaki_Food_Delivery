package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {
    public void createPayment(int orderId, String method, String status) throws Exception {
        String sql = "INSERT INTO payments(order_id, method, status) VALUES(?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, method);
            ps.setString(3, status);
            ps.executeUpdate();
        }
    }

    public List<Payment> getPaymentsByRestaurant(int restaurantId) throws Exception {
        String sql = """
                    SELECT p.* FROM payments p
                    JOIN orders o ON p.order_id = o.id
                    WHERE o.restaurant_id = ?
                """;
        List<Payment> list = new ArrayList<>();
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("method"),
                        rs.getString("status")));
            }
        }
        return list;
    }
}