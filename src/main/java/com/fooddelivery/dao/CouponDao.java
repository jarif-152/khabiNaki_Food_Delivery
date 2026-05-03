package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Coupon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDao {

    public Coupon findByCode(String code) throws Exception {
        String sql = "SELECT * FROM coupons WHERE code = ?";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        }
        return null;
    }

    public void addCoupon(String code, int discountPercent) throws Exception {
        String sql = "INSERT INTO coupons(code, discount_percent) VALUES(?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code.toUpperCase());
            ps.setInt(2, discountPercent);
            ps.executeUpdate();
        }
    }

    public List<Coupon> getAllCoupons() throws Exception {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY id";
        try (Connection con = DbConfig.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(map(rs));
        }
        return list;
    }

    private Coupon map(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getInt("discount_percent"));
    }
}