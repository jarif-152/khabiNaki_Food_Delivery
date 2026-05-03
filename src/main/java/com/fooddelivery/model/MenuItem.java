package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MenuItem {
    private int id;
    private int restaurantId;
    private String name;
    private double price;
    private boolean available;
    private int quantity;
    private String addons;

    public MenuItem() {
    }

    public MenuItem(int id, int restaurantId, String name, double price,
            boolean available, int quantity, String addons) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.available = available;
        this.quantity = quantity;
        this.addons = addons;
    }

    public int getId() {
        return id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAddons() {
        return addons;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAddons(String addons) {
        this.addons = addons;
    }
}