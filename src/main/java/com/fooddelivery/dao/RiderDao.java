package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Rider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiderDao {

    public Rider getAvailableRider() throws Exception {
        String sql = "SELECT * FROM riders WHERE available=true LIMIT 1";
        try (Connection con = DbConfig.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next())
                return map(rs);
        }
        return null;
    }

    public List<Rider> getAllRiders() throws Exception {
        List<Rider> list = new ArrayList<>();
        String sql = "SELECT * FROM riders ORDER BY id";
        try (Connection con = DbConfig.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    public void setAvailable(int riderId, boolean available) throws Exception {
        String sql = "UPDATE riders SET available=? WHERE id=?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, riderId);
            ps.executeUpdate();
        }
    }

    private Rider map(ResultSet rs) throws SQLException {
        return new Rider(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBoolean("available"));
    }
}