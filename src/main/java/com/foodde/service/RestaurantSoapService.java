package com.foodde.service;

import com.foodde.model.Restaurant;
import com.foodde.repository.StorageRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;
import java.util.stream.Collectors;

@WebService(serviceName = "RestaurantService")
public class RestaurantSoapService {
    private static StorageRepository repository;

    public static void setRepository(StorageRepository repo) {
        repository = repo;
    }

    @WebMethod(operationName = "getRestaurantsByArea")
    public List<Restaurant> getRestaurantsByArea(@WebParam(name = "areaName") String areaName) {
        List<Restaurant> all = repository.getAllRestaurants();
        
        // Filter by address area
        return all.stream()
                .filter(r -> r.getAddress().toLowerCase().contains(areaName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @WebMethod(operationName = "getAllRestaurants")
    public List<Restaurant> getAllRestaurants() {
        return repository.getAllRestaurants();
    }
}
