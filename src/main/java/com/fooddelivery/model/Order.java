package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private int id;
    private int customerId;
    private int restaurantId;
    private double totalPrice;
    private String status;

    public Order() {
    }

    public Order(int id, int customerId, int restaurantId, double totalPrice, String status) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}