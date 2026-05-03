package com.fooddelivery.service;

import com.fooddelivery.dao.PaymentDao;

public class PaymentService {
    private final PaymentDao dao = new PaymentDao();

    public void pay(int orderId, String method) throws Exception {
        dao.createPayment(orderId, method, "PAID");
    }
}