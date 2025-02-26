package com.revature.loansp.controller;

import com.revature.loansp.dto.UserDto;
import com.revature.loansp.model.User;
import com.revature.loansp.service.UserService;

import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    public void register(Context ctx) {
        // Get from the body the user to register
        UserDto userInRequest = ctx.bodyAsClass(UserDto.class);

        // Check if we have the required fields
        if(userInRequest.getUsername() == null 
            || userInRequest.getPassword() == null 
            || userInRequest.getRole() == null) {
            ctx.status(400).json("{\"error\":\"Missing username, password or role.\"}");
            return;
        }

        // Register user and verify if it was succesfull or not
        boolean success = userService.registerUser(userInRequest.getUsername(),
                                                    userInRequest.getPassword(),
                                                    userInRequest.getRole());
        if(success) {
            // If true, return this message.
            ctx.status(201).json("{\"message\":\"User registered successfully\"}");
        } else {
            // If false, the username already exists.
            ctx.status(409).json("{\"error\":\"Username already exists\"}");
        }
    }


    public void login(Context ctx) {
        // Get from the body the user that is trying to login
        UserDto userInRequest = ctx.bodyAsClass(UserDto.class);

        // Check if we have all the required fields
        if(userInRequest.getUsername() == null || userInRequest.getPassword() == null) {
            ctx.status(400).json("{\"error\":\"Missing username or password.\"}");
            return;
        }

        // Get the user if it is in the DB, if not, it's null.
        User userFromDB = userService.loginUser(userInRequest.getUsername(), userInRequest.getPassword());
        
        // Check if it's null, if not:
        if (userFromDB != null) {
            // Creates session
            HttpSession session = ctx.req().getSession(true);
            session.setAttribute("user", userFromDB);
            ctx.status(200).json("{\"message\":\"Login successful\"}");

        } else {
            // If it's empty, retunrs the following message.
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
        }
    }

    public void checkLogin(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            ctx.status(200).json("{\"message\":\"You are logged in\"}");
        } else {
            ctx.status(401).json("{\"error\":\"Not logged in\"}");
        }
    }

    public void logout(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        ctx.status(200).json("{\"message\":\"Logged out\"}");
    }
}
