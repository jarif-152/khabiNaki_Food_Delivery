package com.foodde.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "restaurant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Restaurant {
    @XmlElement
    private String id;
    
    @XmlElement
    private String name;
    
    @XmlElement
    private String address;
    
    @XmlElement
    private String schedule; // e.g., "9:00 AM - 10:00 PM"
    
    @XmlElementWrapper(name = "menu")
    @XmlElement(name = "item")
    private List<MenuItem> menu = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String id, String name, String address, String schedule) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.schedule = schedule;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public List<MenuItem> getMenu() { return menu; }
    public void setMenu(List<MenuItem> menu) { this.menu = menu; }
}
