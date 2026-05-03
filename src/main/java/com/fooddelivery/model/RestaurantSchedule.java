package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RestaurantSchedule {
    private int id;
    private int restaurantId;
    private String openTime;
    private String closeTime;

    public RestaurantSchedule() {
    }

    public RestaurantSchedule(int id, int restaurantId, String openTime, String closeTime) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public int getId() {
        return id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}