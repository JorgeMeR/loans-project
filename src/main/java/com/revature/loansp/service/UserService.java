package com.revature.loansp.service;

import com.revature.loansp.model.User;
import com.revature.loansp.dao.UserDao;
import com.revature.loansp.dto.UserDto;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean registerUser(String username, String rawPassword, String role) {
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(4));

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
        
        if(!BCrypt.checkpw(rawPassword, existingUser.getPasswordHash())){
            return null;
        }
        return existingUser;
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public User updateUser(int id, UserDto userDto) {
        // Make the casting
        User user = new User(userDto.getId(), userDto.getUsername(), userDto.getPassword(), userDto.getRole());
        user.setId(id);
        if(user.getPasswordHash() != null){
            String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt(4));
            user.setPasswordHash(hashedPassword);
        }
        boolean isUserUpdated = userDao.updateUser(user);
        if(isUserUpdated) {
            return userDao.getUserById(user.getId());
        }
        return null;
    }
}
