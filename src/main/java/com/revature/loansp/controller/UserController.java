package com.revature.loansp.controller;

import java.util.List;

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
            // Creates session, the true value is to create
            HttpSession session = ctx.req().getSession(true);
            // Pass the user into the session.
            session.setAttribute("user", userFromDB);
            ctx.status(200).json("{\"message\":\"Login successful\"}");

        } else {
            // If it's empty, retunrs the following message.
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
        }
    }

    public void checkLogin(Context ctx) {
        // Get the session passing false and not generate a new one.
        HttpSession session = ctx.req().getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            ctx.status(200).json("{\"message\":\"You are logged in\"}");
        } else {
            ctx.status(401).json("{\"error\":\"Not logged in\"}");
        }
    }

    public void logout(Context ctx) {
        // Get the session passing false and not generate a new one.
        HttpSession session = ctx.req().getSession(false);
        // If there is a session, we invalidate it.
        if (session != null) {
            session.invalidate();
        }
        // After that, send the message
        ctx.status(200).json("{\"message\":\"Logged out\"}");
    }

    public void getUser(Context ctx) {
        // Get the session passing false and not generate a new one.
        HttpSession session = ctx.req().getSession(false);
        // Check if there is no session or it doesn't have a user
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int userIdRequested = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        User userRequested = userService.getUserById(userIdRequested);
        // If the user has a manager role from the session or is the same user
        // it returs the info from the user.
        // If not, returns error and a message.
        if(activeUser.getRole().equals("manager")) {
            ctx.json(userRequested);
            return;
        } else if (activeUser.getId() == userIdRequested) {
            ctx.json(userRequested);
            return;
        } else {
            ctx.status(401).json("{\"error\":\"You don't have the right privileges.\"}");
            return;
        }
    }


    public void updateUser(Context ctx) {
        // Get the session passing false and not generate a new one.
        HttpSession session = ctx.req().getSession(false);
        // Check if there is no session or it doesn't have a user
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }

        // Get from the body the user to register
        UserDto userDto = ctx.bodyAsClass(UserDto.class);
        // Make the casting
        User userInRequest = new User(userDto.getId(), userDto.getUsername(), userDto.getPassword(), userDto.getRole());
        // Check if we have the required fields
        if(userInRequest.getUsername() == null 
            || userInRequest.getPasswordHash() == null 
            || userInRequest.getRole() == null) {
            ctx.status(400).json("{\"error\":\"Missing username, password or role.\"}");
            return;
        }

        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int userIdInPath = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        User userInPath = userService.getUserById(userIdInPath);

        if(userInPath == null) {
            ctx.status(401).json("{\"error\":\"The user that you are trying to modify doesnt exists.\"}");
            return;
        }
        // If the user has a manager role from the session or is the same user
        // it returs the info from the user.
        // If not, returns error and a message.
        if(activeUser.getRole().equals("manager") || activeUser.getId() == userIdInPath) {
            userInPath = userService.updateUser(userIdInPath, userInRequest);
            ctx.json(userInPath);
            return;
        }
        ctx.status(401).json("{\"error\":\"You don't have the right privileges.\"}");
        return;
    }
}
