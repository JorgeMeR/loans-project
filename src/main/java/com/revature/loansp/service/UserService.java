package com.revature.loansp.service;

import com.revature.loansp.model.User;
import com.revature.loansp.dao.UserDao;

public class UserService {
    
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean registerUser(String username, String rawPassword, String role) {
        String hashedPassword = "HASHED_" + rawPassword;

        if(userDao.getUserByName(username) != null) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(role);
        userDao.createUser(newUser);
        return true;
    }

    public User loginUser(String username, String rawPassword) {
        User existingUser = userDao.getUserByName(username);
        if(existingUser == null) {
            return null;
        }

        String hashedPassword = "HASHED_" + rawPassword;
        if(!hashedPassword.equals(existingUser.getPasswordHash())){
            return null;
        }
        return existingUser;
    }
}
