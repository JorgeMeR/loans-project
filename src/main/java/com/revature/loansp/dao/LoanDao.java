package com.revature.loansp.dao;

import com.revature.loansp.model.Loan;

import java.sql.*;
import java.util.*;

public class LoanDao {
    private final String url;
    private final String dbUser;
    private final String dbPassword;
    

    public LoanDao(String url, String dbUser, String dbPassword) {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }


    public Loan createLoan(Loan newLoan) {
        String sql = "INSERT INTO loans (user_id, amount) VALUES (?, ?)";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setInt(1, newLoan.getUserId());
                stmt.setInt(2, newLoan.getAmount());

                stmt.executeUpdate();

                try(ResultSet generatedKey = stmt.getGeneratedKeys()) {
                    if(generatedKey.next()) {
                        int newId = generatedKey.getInt(1);
                        newLoan.setId(newId);
                        // Check with the database if the status can be get
                        newLoan.setStatus("pending");
                    }
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return newLoan;
    }

    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, userId);
                ResultSet res = stmt.executeQuery();

                while(res.next()) {
                    loans.add(new Loan(
                                res.getInt(1),
                                res.getInt(2),
                                res.getInt(3),
                                res.getString(4)
                                ));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }


    public Loan getLoanByLoanId(int LoanId) {
        String sql = "SELECT * FROM loans WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, LoanId);
                ResultSet res = stmt.executeQuery();

                if(res.next()) {
                    Loan loan = new Loan(
                                res.getInt(1),
                                res.getInt(2),
                                res.getInt(3),
                                res.getString(4)
                                );
                    return loan;
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                ResultSet res = stmt.executeQuery();

                while(res.next()){
                    loans.add(new Loan(
                                res.getInt(1),
                                res.getInt(2),
                                res.getInt(3),
                                res.getString(4)
                    ));
                }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public void updateLoan(Loan loan) {
        String sql = "UPDATE loans SET amount = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, loan.getAmount());
                stmt.setInt(2, loan.getId());

                stmt. executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveLoan(int loanId) {
        String sql = "UPDATE loans SET status = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "approved");
                stmt.setInt(2, loanId);

                stmt. executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void rejectLoan(int loanId) {
        String sql = "UPDATE loans SET status = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "rejected");
                stmt.setInt(2, loanId);

                stmt. executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
