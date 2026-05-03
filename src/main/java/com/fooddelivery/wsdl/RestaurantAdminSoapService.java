package com.fooddelivery.wsdl;

import com.fooddelivery.dao.*;
import com.fooddelivery.model.*;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://wsdl.fooddelivery.com/")
public class RestaurantAdminSoapService {
    private final RestaurantDao restaurantDao = new RestaurantDao();
    private final MenuItemDao menuItemDao = new MenuItemDao();
    private final ScheduleDao scheduleDao = new ScheduleDao();
    private final PaymentDao paymentDao = new PaymentDao();
    private final RiderDao riderDao = new RiderDao();
    private final DeliveryDao deliveryDao = new DeliveryDao();
    private final OrderDao orderDao = new OrderDao();
    private final CouponDao couponDao = new CouponDao();

    // restaurant methods

    @WebMethod
    public void registerRestaurant(
            @WebParam(name = "ownerId") int ownerId,
            @WebParam(name = "name") String name,
            @WebParam(name = "address") String address) throws Exception {
        restaurantDao.addRestaurant(ownerId, name, address);
    }

    // menus

    @WebMethod
    public void addMenuItem(
            @WebParam(name = "restaurantId") int restaurantId,
            @WebParam(name = "name") String name,
            @WebParam(name = "price") double price,
            @WebParam(name = "quantity") int quantity,
            @WebParam(name = "addons") String addons) throws Exception {
        menuItemDao.addMenuItem(restaurantId, name, price, quantity, addons);
    }

    @WebMethod
    public void setMenuAvailability(
            @WebParam(name = "menuItemId") int menuItemId,
            @WebParam(name = "available") boolean available) throws Exception {
        menuItemDao.updateAvailability(menuItemId, available);
    }

    @WebMethod
    public void setMenuQuantity(
            @WebParam(name = "menuItemId") int menuItemId,
            @WebParam(name = "quantity") int quantity) throws Exception {
        menuItemDao.updateQuantity(menuItemId, quantity);
    }

    @WebMethod
    public void setMenuAddons(
            @WebParam(name = "menuItemId") int menuItemId,
            @WebParam(name = "addons") String addons) throws Exception {
        menuItemDao.updateAddons(menuItemId, addons);
    }

    @WebMethod
    public void deleteMenuItem(
            @WebParam(name = "menuItemId") int menuItemId) throws Exception {
        menuItemDao.deleteMenuItem(menuItemId);
    }

    @WebMethod
    public void setSchedule(
            @WebParam(name = "restaurantId") int restaurantId,
            @WebParam(name = "openTime") String openTime,
            @WebParam(name = "closeTime") String closeTime) throws Exception {
        scheduleDao.setSchedule(restaurantId, openTime, closeTime);
    }

    @WebMethod
    public RestaurantSchedule getSchedule(
            @WebParam(name = "restaurantId") int restaurantId) throws Exception {
        return scheduleDao.getSchedule(restaurantId);
    }

    // order method
    @WebMethod
    public List<Order> getOrdersByRestaurant(
            @WebParam(name = "restaurantId") int restaurantId) throws Exception {
        return orderDao.getOrdersByRestaurant(restaurantId);
    }

    // payments

    @WebMethod
    public List<Payment> getPaymentsByRestaurant(
            @WebParam(name = "restaurantId") int restaurantId) throws Exception {
        return paymentDao.getPaymentsByRestaurant(restaurantId);
    }

    // riders
    @WebMethod
    public List<Rider> getAllRiders() throws Exception {
        return riderDao.getAllRiders();
    }

    @WebMethod
    public void assignRiderToOrder(
            @WebParam(name = "orderId") int orderId,
            @WebParam(name = "riderId") int riderId) throws Exception {
        deliveryDao.createDelivery(orderId, riderId, "OUT_FOR_DELIVERY");
        riderDao.setAvailable(riderId, false);
    }

    // coupons
    @WebMethod
    public void addCoupon(
            @WebParam(name = "code") String code,
            @WebParam(name = "discountPercent") int discountPercent) throws Exception {
        couponDao.addCoupon(code, discountPercent);
    }

    @WebMethod
    public List<Coupon> getAllCoupons() throws Exception {
        return couponDao.getAllCoupons();
    }
}