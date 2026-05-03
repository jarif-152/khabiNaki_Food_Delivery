package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Delivery;

import java.sql.*;

public class DeliveryDao {

    public void createDelivery(int orderId, int riderId, String progress) throws Exception {
        String sql = "INSERT INTO deliveries(order_id, rider_id, progress) VALUES(?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, riderId);
            ps.setString(3, progress);
            ps.executeUpdate();
        }
    }

    public void updateProgress(int orderId, String progress) throws Exception {
        String sql = "UPDATE deliveries SET progress=? WHERE order_id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, progress);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public Delivery getDeliveryByOrderId(int orderId) throws Exception {
        String sql = "SELECT * FROM deliveries WHERE order_id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Delivery(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("rider_id"),
                        rs.getString("progress"));
            }
        }
        return null;
    }
}