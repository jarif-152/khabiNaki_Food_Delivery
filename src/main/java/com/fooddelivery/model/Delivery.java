package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Delivery {
    private int id;
    private int orderId;
    private int riderId;
    private String progress;

    // Required: DeliveryDao.getDeliveryByOrderId() constructs this; no-arg needed
    // by JAX-WS
    public Delivery() {
    }

    public Delivery(int id, int orderId, int riderId, String progress) {
        this.id = id;
        this.orderId = orderId;
        this.riderId = riderId;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getRiderId() {
        return riderId;
    }

    public String getProgress() {
        return progress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}