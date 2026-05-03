package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.RestaurantSchedule;

import java.sql.*;

public class ScheduleDao {

    public void setSchedule(int restaurantId, String openTime, String closeTime) throws Exception {
        String sql = "INSERT INTO restaurant_schedules(restaurant_id, open_time, close_time) " +
                "VALUES(?,?,?) ON CONFLICT (restaurant_id) DO UPDATE " +
                "SET open_time=EXCLUDED.open_time, close_time=EXCLUDED.close_time";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ps.setString(2, openTime);
            ps.setString(3, closeTime);
            ps.executeUpdate();
        }
    }

    public RestaurantSchedule getSchedule(int restaurantId) throws Exception {
        String sql = "SELECT * FROM restaurant_schedules WHERE restaurant_id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RestaurantSchedule(
                        rs.getInt("id"),
                        rs.getInt("restaurant_id"),
                        rs.getString("open_time"),
                        rs.getString("close_time"));
            }
        }
        return null;
    }
}