package com.revature.loansp;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.revature.loansp.dao.LoanDao;
import com.revature.loansp.dao.UserDao;

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
                ('admin', 'HASHED_admin', 'manager'),
                ('jorgemer', 'HASHED_password', 'regular');

            -- Insert sample loan
            INSERT INTO loans (user_id, amount, interest)
            VALUES 
                (2, '1000', 7.5)
            """;

    
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/loans_db";
        String dbUser = "postgres";
        String dbPassword = "password";

        resetDB(jdbcUrl, dbUser, dbPassword);

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