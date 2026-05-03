package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItem {
    private int id;
    private int orderId;
    private int menuItemId;
    private int quantity;

    // Required: OrderItemDao.getOrderItems() constructs this
    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int menuItemId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}