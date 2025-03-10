package com.revature.loansp;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.javalin.Javalin;

import com.revature.loansp.dao.LoanDao;
import com.revature.loansp.dao.UserDao;
import com.revature.loansp.service.UserService;
import com.revature.loansp.service.LoanService;
import com.revature.loansp.controller.UserController;
import com.revature.loansp.controller.LoanController;


public class Main {
    private static final String DROP_TABLES_SQL = """
            DROP TABLE IF EXISTS loans CASCADE;
            DROP TABLE IF EXISTS users CASCADE;
            """;
    
    private static final String CREATE_TABLES_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                role VARCHAR(50) NOT NULL DEFAULT 'regular'
            );

            CREATE TABLE IF NOT EXISTS loans (
            id SERIAL PRIMARY KEY,
            user_id INT NOT NULL,
            amount INT NOT NULL,
            interest REAL NOT NULL,
            status VARCHAR(50) DEFAULT 'pending',
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;
    
    private static final String INSERT_DATA_SQL = """
            -- Insert admin user and regular user
            INSERT INTO users (username, password_hash, role)
            VALUES 
                ('admin', '$2a$04$6EKj/wnPLYsVYfXXlinSwupaHkaKPCyMKKM7yvsjRn/vEuRrev.l.', 'manager'),
                ('jorgemer', '$2a$04$i7KbX/I1IyPp/M4d/TToAeat/pb6NyhFzLMJML0uWcvi9cjw1NAUO', 'regular'),
                ('champs', '$2a$04$FWD2j2P9XTwrnVLVPKFr9Oo09V6Cf6p63CPuHDE9uuEUIBrtsH8yG', 'regular');

            -- Insert sample loan
            INSERT INTO loans (user_id, amount, interest)
            VALUES 
                (2, 1000, 7.5),
                (2, 3000, 4.3),
                (3, 4000, 8.2);
            """;

    
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/loans_db";
        String dbUser = "postgres";
        String dbPassword = "password";

        resetDB(jdbcUrl, dbUser, dbPassword);

        UserDao userDao = new UserDao(jdbcUrl, dbUser, dbPassword);
        LoanDao loanDao = new LoanDao(jdbcUrl, dbUser, dbPassword);

        UserService userService = new UserService(userDao);
        LoanService loanService = new LoanService(loanDao);

        UserController userController = new UserController(userService);
        LoanController loanController = new LoanController(loanService);


        Javalin app = Javalin.create(config -> {}).start(7000);

        app.post("/auth/register", userController::register);
        app.post("/auth/login", userController::login);
        app.post("/auth/logout", userController::logout);
        app.post("/auth/check", userController::checkLogin);

        app.get("/users/{id}", userController::getUser);
        app.put("/users/{id}", userController::updateUser);

        app.get("/loans", loanController::getLoans);
        app.post("/loans", loanController::createLoan);
        app.get("/loans/{id}", loanController::viewLoan);
        app.put("/loans/{id}", loanController::updateLoan);
        app.put("/loans/{id}/approve", loanController::approveLoan);
        app.put("/loans/{id}/reject", loanController::rejectLoan);

        System.out.println("Server running on http://localhost:7000/");

    }

    // TODO: Check if I can leave it as public or it should stay as private
    public static void resetDB(String jdbcUrl, String dbUser, String dbPassword) {
        runSqlStmt(DROP_TABLES_SQL, jdbcUrl, dbUser, dbPassword);
        runSqlStmt(CREATE_TABLES_SQL, jdbcUrl, dbUser, dbPassword);
        runSqlStmt(INSERT_DATA_SQL, jdbcUrl, dbUser, dbPassword);
    }


    private static void runSqlStmt(String sql, String jdbcUrl, String dbUser, String dbPassword) {
        try(Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Executed SQL:\n" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}