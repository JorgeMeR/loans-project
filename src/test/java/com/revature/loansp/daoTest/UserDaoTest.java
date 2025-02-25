package com.revature.loansp.daoTest;

import com.revature.loansp.Main;
import com.revature.loansp.dao.UserDao;
import com.revature.loansp.model.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {
    public UserDao userDao;
    public String url = "jdbc:postgresql://localhost:5432/loans_db";
    public String dbUser = "postgres";
    public String dbPassword = "password";

    @Before
    public void setUp() {
        Main.resetDB(url, dbUser, dbPassword);
        userDao = new UserDao(url, dbUser, dbPassword);
    }


    // Check that we can get correctly the user
    @Test
    public void getUserByNameTest(){
        // Arrange
        String username = "jorgemer";
        // Act
        User user = userDao.getUserByName(username);
        // Assert
        if(user == null) {
            Assert.fail();
        } else {
            User userJorgemer = new User(2, "jorgemer", "HASHED_password", "regular");
            Assert.assertTrue(user.equals(userJorgemer));
        }
    }

    // Adding a new user
    @Test
    public void createUserTest() {
        // Arrange
        User user = new User();
        user.setUsername("jessgam");
        user.setPasswordHash("HASHED_password2");
        user.setRole("regular");
        User expectedUser = new User(4, "jessgam", "HASHED_password2", "regular");
        // Act
        userDao.createUser(user);
        // Assert
        Assert.assertTrue(user.equals(expectedUser));
    }

    // Updating an user
    @Test
    public void updateUserTest() {
        // Arrange
        User user = new User(2,"richimer", "HASHED_password3", "manager");
        // Act
        userDao.updateUser(user);
        User updatedUser = userDao.getUserByName("richimer");
        // Assert
        Assert.assertTrue(user.equals(updatedUser));
    }
}
