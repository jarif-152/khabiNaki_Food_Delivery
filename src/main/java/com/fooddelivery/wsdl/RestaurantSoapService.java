package com.fooddelivery.wsdl;

import com.fooddelivery.dao.CustomerDao;
import com.fooddelivery.model.Customer;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.MenuService;
import com.fooddelivery.service.RestaurantService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://wsdl.fooddelivery.com/")
public class RestaurantSoapService {
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuService menuService = new MenuService();
    private final CustomerDao customerDao = new CustomerDao();

    @WebMethod
    public List<Customer> getAllCustomers() throws Exception {
        return customerDao.getAllCustomers();
    }

    @WebMethod
    public List<Restaurant> getAllRestaurants() throws Exception {
        return restaurantService.getAllRestaurantsSorted();
    }

    @WebMethod
    public List<Restaurant> getRestaurantsByArea(@WebParam(name = "area") String area) throws Exception {
        return restaurantService.findByAreaSorted(area == null ? "" : area.trim());
    }

    @WebMethod
    public List<MenuItem> getMenuByRestaurant(@WebParam(name = "restaurantId") int restaurantId) throws Exception {
        return menuService.getAvailableMenu(restaurantId);
    }

    @WebMethod
    public void registerCustomer(
            @WebParam(name = "name") String name,
            @WebParam(name = "phone") String phone,
            @WebParam(name = "address") String address) throws Exception {
        customerDao.addCustomer(name, phone, address);
    }
}