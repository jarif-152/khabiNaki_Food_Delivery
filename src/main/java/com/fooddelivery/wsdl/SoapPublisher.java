package com.fooddelivery.wsdl;

import jakarta.xml.ws.Endpoint;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SoapPublisher {
    public static void publish() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            Endpoint restaurantEndpoint = Endpoint.create(new RestaurantSoapService());
            Endpoint orderEndpoint = Endpoint.create(new OrderSoapService());
            Endpoint adminEndpoint = Endpoint.create(new RestaurantAdminSoapService());

            HttpContext restaurantContext = server.createContext("/RestaurantService");
            HttpContext orderContext = server.createContext("/OrderService");
            HttpContext adminContext = server.createContext("/AdminService");

            Filter corsFilter = new CorsFilter();
            restaurantContext.getFilters().add(corsFilter);
            orderContext.getFilters().add(corsFilter);
            adminContext.getFilters().add(corsFilter);

            restaurantEndpoint.publish(restaurantContext);
            orderEndpoint.publish(orderContext);
            adminEndpoint.publish(adminContext);

            server.start();

            System.out.println("RestaurantService: http://localhost:8080/RestaurantService?wsdl");
            System.out.println("OrderService: http://localhost:8080/OrderService?wsdl");
            System.out.println("AdminService: http://localhost:8080/AdminService?wsdl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CorsFilter extends Filter {
        @Override
        public String description() {
            return "CORS Filter";
        }

        @Override
        public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            chain.doFilter(exchange);
        }
    }
}