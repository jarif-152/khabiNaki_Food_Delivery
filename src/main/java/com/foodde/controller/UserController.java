package com.foodde.controller;

import com.foodde.model.User;
import io.javalin.http.Context;
import java.util.Map;
import java.util.UUID;

public class UserController {
    // Show login/profile form
    public void showLogin(Context ctx) {
        ctx.render("templates/login.html");
    }

    // Process login form
    public void login(Context ctx) {
        String name = ctx.formParam("name");
        String address = ctx.formParam("address");
        String phone = ctx.formParam("phone");

        // Create a user for this session
        User user = new User(UUID.randomUUID().toString(), name, address, phone);
        ctx.sessionAttribute("user", user);

        // Redirect back to cart or home
        ctx.redirect("/cart");
    }
}
