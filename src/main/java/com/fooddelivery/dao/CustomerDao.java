package com.fooddelivery.dao;

import com.fooddelivery.config.DbConfig;
import com.fooddelivery.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    public void addCustomer(String name, String phone, String address) throws Exception {
        String sql = "INSERT INTO customers(name, phone, address) VALUES(?,?,?)";
        try (Connection con = DbConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.executeUpdate();
        }
    }

    public List<Customer> getAllCustomers() throws Exception {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection con = DbConfig.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")));
            }
        }
        return list;
    }
}