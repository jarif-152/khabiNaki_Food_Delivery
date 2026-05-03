package com.fooddelivery.service;

import com.fooddelivery.dao.*;
import com.fooddelivery.model.Coupon;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDao orderDao = new OrderDao();
    private final OrderItemDao orderItemDao = new OrderItemDao();
    private final MenuItemDao menuItemDao = new MenuItemDao();
    private final CouponDao couponDao = new CouponDao();

    public int placeOrder(int customerId, int restaurantId, int menuItemId, int quantity, String couponCode)
            throws Exception {
        double pricePerItem = menuItemDao.getMenuByRestaurant(restaurantId).stream()
                .filter(i -> i.getId() == menuItemId)
                .findFirst()
                .orElseThrow()
                .getPrice();

        double total = pricePerItem * quantity;

        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = couponDao.findByCode(couponCode);
            if (coupon != null) {
                total = total - (total * coupon.getDiscountPercent() / 100.0);
            }
        }

        int orderId = orderDao.createOrder(customerId, restaurantId, total, "PLACED");
        orderItemDao.addOrderItem(orderId, menuItemId, quantity);
        menuItemDao.reduceQuantity(menuItemId, quantity);
        return orderId;
    }

    public void updateStatus(int orderId, String status) throws Exception {
        orderDao.updateStatus(orderId, status);
    }

    public void cancelOrder(int orderId) throws Exception {
        Order order = orderDao.getOrderById(orderId);
        if (order == null)
            throw new Exception("Order not found: " + orderId);

        String status = order.getStatus();
        if ("OUT_FOR_DELIVERY".equalsIgnoreCase(status) || "DELIVERED".equalsIgnoreCase(status)) {
            throw new Exception("Cannot cancel an order that is already " + status);
        }
        if ("CANCELLED".equalsIgnoreCase(status)) {
            throw new Exception("Order is already cancelled.");
        }

        List<OrderItem> items = orderItemDao.getOrderItems(orderId);
        for (OrderItem item : items) {
            menuItemDao.increaseQuantity(item.getMenuItemId(), item.getQuantity());
        }

        orderDao.updateStatus(orderId, "CANCELLED");
    }
}