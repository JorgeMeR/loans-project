package com.revature.loansp.daoTest;

import com.revature.loansp.Main;
import com.revature.loansp.dao.LoanDao;
import com.revature.loansp.model.Loan;

import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class LoanDaoTest {
    public LoanDao loanDao;
    public String url = "jdbc:postgresql://localhost:5432/loans_db";
    public String dbUser = "postgres";
    public String dbPassword = "password";

    @Before
    public void setUp() {
        Main.resetDB(url, dbUser, dbPassword);
        loanDao = new LoanDao(url, dbUser, dbPassword);
    }

    @Test
    public void getLoanByLoanIdTest() {
        // Arrange
        int loanId = 1;
        // Act
        Loan gotLoan = loanDao.getLoanByLoanId(loanId);
        // Assert
        if(gotLoan == null) {
            Assert.fail();
        } else {
            Loan expectedLoan = new Loan(1, 2, 1000, 7.0f, "pending");
            Assert.assertTrue(gotLoan.equals(expectedLoan));
        }

    }

    @Test
    public void createLoanTest() {
        // Arrange
        Loan newLoan = new Loan();
        newLoan.setUserId(2);
        newLoan.setAmount(3000);
        newLoan.setInterest(4.3f);
        Loan expectedLoan = new Loan(4, 2, 3000, 4.3f, "pending");
        // Act
        loanDao.createLoan(newLoan);
        // Assert
        Assert.assertTrue(newLoan.equals(expectedLoan));
    }

    
    @Test
    public void getLoansByUserIdTest() {
        // Arrange
        int user_id = 2;
        List<Loan> expectedLoans = new ArrayList<>();
        expectedLoans.add(new Loan(1, 2, 1000, 7.5f, "pending"));
        expectedLoans.add(new Loan(2, 2, 3000, 4.3f, "pending"));
        //Act
        List<Loan> userLoans = loanDao.getLoansByUserId(user_id);
        // Assert
        Assert.assertEquals(expectedLoans, userLoans);
    }
    
    @Test
    public void getAllLoansTest() {
        // Arrange
        List<Loan> expectedLoans = new ArrayList<>();
        expectedLoans.add(new Loan(1, 2, 1000, 7.5f, "pending"));
        expectedLoans.add(new Loan(2, 2, 3000, 4.3f, "pending"));
        expectedLoans.add(new Loan(3, 3, 4000, 8.2f, "pending"));
        // Act
        List<Loan> allLoans = loanDao.getAllLoans();
        // Assert
        Assert.assertEquals(expectedLoans, allLoans);
    }


    @Test
    public void updateLoanTest() {
        //Arrange
        Loan loanExpected = new Loan(2, 2, 3500, 4.6f, "pending");
        // Act
        loanDao.updateLoan(loanExpected);
        Loan updatedLoan = loanDao.getLoanByLoanId(2);
        // Assert
        Assert.assertEquals(loanExpected, updatedLoan);

    }

    @Test
    public void approveLoanTest() {
        // Arrange
        int loanId = 3;
        Loan expectedLoan = new Loan(3, 3, 4000, 8.2f, "approved");
        // Act
        loanDao.approveLoan(loanId);
        Loan approvedLoan = loanDao.getLoanByLoanId(loanId);
        // Assert
        Assert.assertEquals(expectedLoan, approvedLoan);
    }

    @Test
    public void rejectLoanTest() {
        // Arrange
        int loanId = 3;
        Loan expectedLoan = new Loan(3, 3, 4000, 8.2f, "rejected");
        // Act
        loanDao.rejectLoan(loanId);
        Loan rejectedLoan = loanDao.getLoanByLoanId(loanId);
        // Assert
        Assert.assertEquals(expectedLoan, rejectedLoan);
    }

}
