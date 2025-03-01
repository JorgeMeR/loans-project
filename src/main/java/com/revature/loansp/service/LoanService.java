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
        System.out.println(user.getRole());
        if (user.getRole().equals("manager")) {
            return loanDao.getAllLoans();
        }
        else {
            return loanDao.getLoansByUserId(user.getId());
        }
    }
}
