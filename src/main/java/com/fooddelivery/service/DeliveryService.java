package com.fooddelivery.service;

import com.fooddelivery.dao.DeliveryDao;
import com.fooddelivery.dao.RiderDao;
import com.fooddelivery.model.Delivery;
import com.fooddelivery.model.Rider;

public class DeliveryService {
    private final DeliveryDao deliveryDao = new DeliveryDao();
    private final RiderDao riderDao = new RiderDao();

    public void assignRider(int orderId) throws Exception {
        Rider rider = riderDao.getAvailableRider();
        if (rider != null) {
            deliveryDao.createDelivery(orderId, rider.getId(), "OUT_FOR_DELIVERY");
            riderDao.setAvailable(rider.getId(), false);
        } else {
            throw new Exception("No available riders at the moment.");
        }
    }

    public void updateProgress(int orderId, String progress) throws Exception {

        deliveryDao.updateProgress(orderId, progress);
        if ("DELIVERED".equalsIgnoreCase(progress)) {
            Delivery delivery = deliveryDao.getDeliveryByOrderId(orderId);
            if (delivery != null) {
                riderDao.setAvailable(delivery.getRiderId(), true);
            }
        }
    }
}