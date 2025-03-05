package com.revature.loansp.serviceTest;

import com.revature.loansp.model.User;
import com.revature.loansp.dto.UserDto;
import com.revature.loansp.dao.UserDao;
import com.revature.loansp.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Before
    public void initMocks() {
        //MockitoAnnotations.initMocks();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserByIdTest() {
        // Arrange
        int userId = 2;
        int userIdNull = 4;
        User expectedUser = new User(2, "jorgemer", "$2a$04$i7KbX/I1IyPp/M4d/TToAeat/pb6NyhFzLMJML0uWcvi9cjw1NAUO", "regular");
        when(userDao.getUserById(userId)).thenReturn(expectedUser);
        when(userDao.getUserById(userIdNull)).thenReturn(null);

        // Act
        User user = userService.getUserById(userId);
        User userNull = userService.getUserById(userIdNull);

        // Assert
        Assert.assertTrue(user.equals(expectedUser));
        Assert.assertNull(userNull);
    }

    @Test
    public void registerUserTest() {
        // Arrange
        String username1 = "garzuz";
        String rawPassword1 = "passwordgartz";
        String role1 = "regular";
        User existingUser = new User(2, "jorgemer", "$2a$04$i7KbX/I1IyPp/M4d/TToAeat/pb6NyhFzLMJML0uWcvi9cjw1NAUO", "regular");
        when(userDao.getUserByName(username1)).thenReturn(null);
        when(userDao.getUserByName(existingUser.getUsername())).thenReturn(existingUser);

        // Act
        boolean isRegistered1 = userService.registerUser(username1, rawPassword1, role1);
        boolean isRegistered2 = userService.registerUser(existingUser.getUsername(),existingUser.getPasswordHash(), existingUser.getRole());

        // Assert
        Assert.assertTrue(isRegistered1);
        Assert.assertFalse(isRegistered2);
    }

    @Test
    public void loginUserTest() {
        // Arrange
        String username = "jorgemer";
        String rawPassword = "password";
        String wrongPassword = "wrongPassword";
        String inexistingUser = "garzuz";
        User existingUser = new User(2, "jorgemer", "$2a$04$i7KbX/I1IyPp/M4d/TToAeat/pb6NyhFzLMJML0uWcvi9cjw1NAUO", "regular");
        when(userDao.getUserByName(username)).thenReturn(existingUser);
        when(userDao.getUserByName(inexistingUser)).thenReturn(null);

        // Act
        User verifiedUser = userService.loginUser(username, rawPassword);
        User wrongPasswordUser = userService.loginUser(username, wrongPassword);
        User noUser = userService.loginUser(inexistingUser, rawPassword);

        // Assert
        Assert.assertTrue(existingUser.equals(verifiedUser));
        Assert.assertNull(wrongPasswordUser);
        Assert.assertNull(noUser);
    }

    @Test
    public void updateUserTest() {
        // Arrange
        //
        int id = 3;
        UserDto newUserInfo = new UserDto();
        newUserInfo.setUsername("coto");
        newUserInfo.setPassword("passwordcoto");
        newUserInfo.setRole("regular");
        //
        User userUpdated = new User(id, newUserInfo.getUsername(), "$2a$04$0tvTJq0dOebifaEwzYoQBuCjb9oyW8NQq24hhkoo4GJmngYhQFsry", "regular");
        // Since an object is being created inside, argThat is used to compare the id 
        // of the inside object, if it is equal, then it will return true
        when(userDao.updateUser(argThat(user -> user.getId() == id))).thenReturn(true);
        when(userDao.getUserById(id)).thenReturn(userUpdated);

        // Act
        User userGot = userService.updateUser(id, newUserInfo);

        // Assert
        Assert.assertTrue(userGot.equals(userUpdated));
    }
}
