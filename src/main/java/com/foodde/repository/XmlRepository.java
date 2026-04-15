package com.foodde.repository;

import com.foodde.model.*;
import jakarta.xml.bind.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlRepository implements StorageRepository {
    private final String dataDir = "data/";

    public XmlRepository() {
        new File(dataDir).mkdirs(); // Ensure "data/" directory exists
    }

    // Helper method to save to XML
    private <T> void saveToFile(T object, String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, new File(dataDir + fileName));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // Data persistence implementation
    @Override
    public void saveRestaurant(Restaurant restaurant) {
        // We save each restaurant in its own file: data/restaurant_ID.xml
        saveToFile(restaurant, "restaurant_" + restaurant.getId() + ".xml");
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        File folder = new File(dataDir);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("restaurant_") && name.endsWith(".xml"));

        if (files != null) {
            for (File file : files) {
                try {
                    JAXBContext context = JAXBContext.newInstance(Restaurant.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    restaurants.add((Restaurant) unmarshaller.unmarshal(file));
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        return restaurants;
    }

    @Override
    public Restaurant getRestaurantById(String id) {
        try {
            File file = new File(dataDir + "restaurant_" + id + ".xml");
            if (!file.exists()) return null;
            
            JAXBContext context = JAXBContext.newInstance(Restaurant.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Restaurant) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            return null;
        }
    }

    @Override
    public void saveUser(User user) {
        saveToFile(user, "user_" + user.getId() + ".xml");
    }

    @Override
    public User getUserById(String id) {
        try {
            File file = new File(dataDir + "user_" + id + ".xml");
            if (!file.exists()) return null;
            
            JAXBContext context = JAXBContext.newInstance(User.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (User) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            return null;
        }
    }

    @Override
    public void saveOrder(Order order) {
        saveToFile(order, "order_" + order.getId() + ".xml");
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orders = new ArrayList<>();
        File folder = new File(dataDir);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("order_") && name.endsWith(".xml"));

        if (files != null) {
            for (File file : files) {
                try {
                    JAXBContext context = JAXBContext.newInstance(Order.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    Order order = (Order) unmarshaller.unmarshal(file);
                    if (order.getUserId().equals(userId)) {
                        orders.add(order);
                    }
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        return orders;
    }
}
