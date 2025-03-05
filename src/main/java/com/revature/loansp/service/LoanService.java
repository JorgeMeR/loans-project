package com.revature.loansp.service;

import com.revature.loansp.model.User;
import com.revature.loansp.model.Loan;

import java.util.List;

import com.revature.loansp.dao.LoanDao;

public class LoanService {
    
    private final LoanDao loanDao;

    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public List<Loan> getUserLoans(User user) {
        if (user.getRole().equals("manager")) {
            return loanDao.getAllLoans();
        }
        else {
            return loanDao.getLoansByUserId(user.getId());
        }
    }

    public boolean createNewLoan(User user, Loan loan) {
        loan.setUserId(user.getId());
        loanDao.createLoan(loan);
        if(loan.getId() != 0) {
            return true;
        }
        return false;
    }

    public Loan getLoan(int loanId) {
        return loanDao.getLoanByLoanId(loanId);
    }

    public Loan updateLoan(Loan loanInDB, Loan newLoanData) {
        loanInDB.setAmount(newLoanData.getAmount());
        loanInDB.setInterest(newLoanData.getInterest());
        boolean isLoanUpdated = loanDao.updateLoan(loanInDB);
        if(isLoanUpdated){
            return loanDao.getLoanByLoanId(loanInDB.getId());
        }
        return null;
    }

    public Loan approveLoanById(int loanId) {
        boolean isApproved = loanDao.approveLoan(loanId);
        if(isApproved) {
            return loanDao.getLoanByLoanId(loanId);
        }
        return null;
    }

    public Loan rejectLoanById(int loanId) {
        boolean isRejected = loanDao.rejectLoan(loanId);
        if(isRejected) {
            return loanDao.getLoanByLoanId(loanId);
        }
        return null;
    }
}
