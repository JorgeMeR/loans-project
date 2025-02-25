package com.revature.loansp.daoTest;

import com.revature.loansp.Main;
import com.revature.loansp.dao.LoanDao;
import com.revature.loansp.model.Loan;

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
    public void getLoansByLoanIdTest() {
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
    
}
