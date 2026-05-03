package com.fooddelivery.wsdl;

import com.fooddelivery.dao.OrderDao;
import com.fooddelivery.model.Order;
import com.fooddelivery.service.DeliveryService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://wsdl.fooddelivery.com/")
public class OrderSoapService {
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();
    private final DeliveryService deliveryService = new DeliveryService();
    private final OrderDao orderDao = new OrderDao();

    @WebMethod
    public int placeOrder(
            @WebParam(name = "customerId") int customerId,
            @WebParam(name = "restaurantId") int restaurantId,
            @WebParam(name = "menuItemId") int menuItemId,
            @WebParam(name = "quantity") int quantity,
            @WebParam(name = "couponCode") String couponCode) throws Exception {
        return orderService.placeOrder(customerId, restaurantId, menuItemId, quantity, couponCode);
    }

    @WebMethod
    public void payOrder(
            @WebParam(name = "orderId") int orderId,
            @WebParam(name = "method") String method) throws Exception {
        paymentService.pay(orderId, method);
        orderService.updateStatus(orderId, "PAID");
    }

    @WebMethod
    public void assignRider(@WebParam(name = "orderId") int orderId) throws Exception {
        deliveryService.assignRider(orderId);
        orderService.updateStatus(orderId, "OUT_FOR_DELIVERY");
    }

    @WebMethod
    public void updateDeliveryProgress(
            @WebParam(name = "orderId") int orderId,
            @WebParam(name = "progress") String progress) throws Exception {
        deliveryService.updateProgress(orderId, progress);
        if ("DELIVERED".equalsIgnoreCase(progress)) {
            orderService.updateStatus(orderId, "DELIVERED");
        }
    }

    @WebMethod
    public Order trackOrder(@WebParam(name = "orderId") int orderId) throws Exception {
        return orderDao.getOrderById(orderId);
    }

    @WebMethod
    public List<Order> getOrdersByCustomer(
            @WebParam(name = "customerId") int customerId) throws Exception {
        return orderDao.getOrdersByCustomer(customerId);
    }

    @WebMethod
    public void cancelOrder(@WebParam(name = "orderId") int orderId) throws Exception {
        orderService.cancelOrder(orderId);
    }
}