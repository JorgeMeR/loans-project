package com.revature.loansp.controller;

import com.revature.loansp.model.User;

import java.util.List;

import com.revature.loansp.model.Loan;
import com.revature.loansp.service.LoanService;

import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

public class LoanController {
    
    private final LoanService loanService;
    
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    public void getLoans(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        List<Loan> userLoans = loanService.getUserLoans(activeUser);
        ctx.json(userLoans);
    }
}
