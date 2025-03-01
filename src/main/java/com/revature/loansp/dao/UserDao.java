package com.revature.loansp.dao;

import com.revature.loansp.model.User;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {
    private final String url;
    private final String dbUser;
    private final String dbPassword;
    private final Logger logger = LoggerFactory.getLogger(UserDao.class);
    

    public UserDao(String url, String dbUser, String dbPassword) {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
 
    
    public User createUser(User newUser) {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                
                stmt.setString(1, newUser.getUsername());
                stmt.setString(2, newUser.getPasswordHash());
                stmt.setString(3, newUser.getRole());

                stmt.executeUpdate();

                try(ResultSet generatedKey = stmt.getGeneratedKeys()){
                    if(generatedKey.next()){
                        int newId = generatedKey.getInt(1);
                        newUser.setId(newId);
                    }
                    logger.info("New user created.");
                }

        }catch (SQLException e){
            logger.error("Exception trying to insert user", e);
            e.printStackTrace();
        }
        return newUser;
    }


    public User getUserByName(String username){
        String sql = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, username);

                try(ResultSet res = stmt.executeQuery()){
                    if(res.next()){
                        logger.debug(username + " info queried.");
                        return new User(res.getInt(1),
                                        res.getString(2),
                                        res.getString(3),
                                        res.getString(4)
                                        );
                    }
                }
        }catch(SQLException e){
            logger.error("Exception trying to get user", e);
            e.printStackTrace();
        }
        return null;
    }


    public User getUserById(int id){
        String sql = "SELECT * FROM users WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);

                try(ResultSet res = stmt.executeQuery()){
                    if(res.next()){
                        logger.debug( "User id " + id + " info queried.");
                        return new User(res.getInt(1),
                                        res.getString(2),
                                        res.getString(3),
                                        res.getString(4)
                                        );
                    }
                }
        }catch(SQLException e){
            logger.error("Exception trying to get user", e);
            e.printStackTrace();
        }
        return null;
    }


    public boolean updateUser(User user){
        String sql = "UPDATE users SET username = ?, password_hash = ?, role = ? WHERE id = ?";
        boolean success;
        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPasswordHash());
                stmt.setString(3, user.getRole());
                stmt.setInt(4, user.getId());
                
                stmt.executeUpdate();
                success = true;
                logger.debug("User updated.");
        
        }catch(SQLException e){
            success = false;
            logger.error("Exception trying to update user", e);
            e.printStackTrace();
        }
        return success;
    }
}
