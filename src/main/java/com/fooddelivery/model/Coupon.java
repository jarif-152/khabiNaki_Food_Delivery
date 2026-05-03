package com.fooddelivery.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Coupon {
    private int id;
    private String code;
    private int discountPercent;

    public Coupon() {
    };

    public Coupon(int id, String code, int discountPercent) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
}