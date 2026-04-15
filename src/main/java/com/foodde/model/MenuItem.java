package com.foodde.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "menuItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class MenuItem {
    @XmlElement
    private String id;
    
    @XmlElement
    private String name;
    
    @XmlElement
    private double price;
    
    @XmlElement
    private String description;
    
    @XmlElement
    private boolean available;

    // Default constructor is REQUIRED for XML libraries
    public MenuItem() {}

    public MenuItem(String id, String name, double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.available = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
