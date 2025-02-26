package com.revature.loansp.service;

import com.revature.loansp.model.User;
import com.revature.loansp.dao.UserDao;

public class UserService {
    
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User registerUser(String username, String rawPassword, String role) {
        String hashedPassword = "HASHED_" + rawPassword;

        if(userDao.getUserByName(username) != null) {
            return null;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(role);
        userDao.createUser(newUser);
        return newUser;
    }
}
