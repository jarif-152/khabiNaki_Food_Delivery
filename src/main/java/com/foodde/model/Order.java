package com.foodde.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    @XmlElement
    private String id;
    
    @XmlElement
    private String userId;
    
    @XmlElement
    private String restaurantId;
    
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<OrderItem> items = new ArrayList<>();
    
    @XmlElement
    private double totalAmount;
    
    @XmlElement
    private String status; // e.g., "Pending", "Preparing", "Delivering", "Delivered"

    public Order() {}

    public Order(String id, String userId, String restaurantId) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.status = "Pending";
    }

    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
