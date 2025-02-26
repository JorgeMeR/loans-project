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
        UserDto userInRequest = ctx.bodyAsClass(UserDto.class);

        if(userInRequest.getUsername() == null || 
            userInRequest.getPassword() == null || 
            userInRequest.getRole() == null) {
            ctx.status(400).json("{\"error\":\"Missing username, password or role.\"}");
            return;
        }

        boolean success = userService.registerUser(userInRequest.getUsername(),
                                                    userInRequest.getPassword(),
                                                    userInRequest.getRole());
        if(success) {
            ctx.status(201).json("{\"message\":\"User registered successfully\"}");
        } else {
            ctx.status(409).json("{\"error\":\"Username already exists\"}");
        }
    }


    public void login(Context ctx) {
        UserDto userInRequest = ctx.bodyAsClass(UserDto.class);

        if(userInRequest.getUsername() == null || userInRequest.getPassword() == null) {
            ctx.status(400).json("{\"error\":\"Missing username or password.\"}");
            return;
        }
        User userFromDB = userService.loginUser(userInRequest.getUsername(), userInRequest.getPassword());
        if (userFromDB != null) {

            HttpSession session = ctx.req().getSession();
            session.setAttribute("user", userFromDB);
            ctx.status(200).json("{\"message\":\"Login successful\"}");
            
        } else {
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
        }
    }
}
