package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDao {

    public void addRestaurant(int ownerId, String name, String address) throws Exception {
        String sql = "INSERT INTO restaurants(owner_id, name, address) VALUES(?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.executeUpdate();
        }
    }

    public List<Restaurant> getAllRestaurants() throws Exception {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM restaurants ORDER BY name";
        try (Connection con = DbConfig.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public List<Restaurant> searchByArea(String area) throws Exception {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM restaurants WHERE address ILIKE ?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + area + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    private Restaurant map(ResultSet rs) throws SQLException {
        return new Restaurant(
                rs.getInt("id"),
                rs.getInt("owner_id"),
                rs.getString("name"),
                rs.getString("address"));
    }
}