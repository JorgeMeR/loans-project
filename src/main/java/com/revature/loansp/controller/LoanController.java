package com.revature.loansp.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.loansp.model.User;
import com.revature.loansp.model.Loan;
import com.revature.loansp.service.LoanService;

import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

public class LoanController {
    
    private final LoanService loanService;
    private final Logger logger = LoggerFactory.getLogger(LoanController.class);
    
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


    public void createLoan(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        Loan loanInBody = ctx.bodyAsClass(Loan.class);
        if(loanInBody.getAmount() == 0
            || loanInBody.getInterest() == 0) {
                ctx.status(400).json("{\"error\":\"Missing amount or interest.\"}");
                return;
        }

        boolean success = loanService.createNewLoan(activeUser, loanInBody);
        if (success) {
            ctx.status(201).json("{\"message\":\"New loan created successfully\"}");
        } else {
            // If false, the username already exists.
            ctx.status(409).json("{\"error\":\"Failure to save new loan\"}");
        }
    }

    public void viewLoan(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int loanIdRequested = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        Loan loanRequested = loanService.getLoan(loanIdRequested);
        if(loanRequested == null) {
            ctx.status(404).json("{\"error\":\"The loan doesn't exists.\"}");
            return;
        }

        if(activeUser.getRole().equals("manager")) {
            ctx.json(loanRequested);
            return;
        } else if (activeUser.getId() == loanRequested.getUserId()) {
            ctx.json(loanRequested);
            return;
        } else {
            logger.warn("Unauthorized access attempt.");
            ctx.status(403).json("{\"error\":\"You don't have the right privileges.\"}");
            return;
        }
    }

    public void updateLoan(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int loanIdInPath = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        Loan loanInPath = loanService.getLoan(loanIdInPath);
        if(loanInPath == null) {
            ctx.status(404).json("{\"error\":\"The loan doesn't exists.\"}");
            return;
        }

        // Get new loan data from the body
        Loan loanInBody = ctx.bodyAsClass(Loan.class);
        if(loanInBody.getAmount() == 0
            || loanInBody.getInterest() == 0) {
                ctx.status(400).json("{\"error\":\"Missing amount or interest.\"}");
                return;
        }

        if(activeUser.getRole().equals("manager")
            || activeUser.getId() == loanInPath.getUserId()) {
            Loan loanUpdated = loanService.updateLoan(loanInPath, loanInBody);
            if (loanUpdated == null) {
                ctx.status(401).json("{\"error\":\"There was an error updating the loan.\"}");
                return;
            }
            ctx.json(loanUpdated);
            return;
        } else {
            logger.warn("Unauthorized access attempt.");
            ctx.status(403).json("{\"error\":\"You don't have the right privileges.\"}");
            return;
        }

    }

    public void approveLoan(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int loanIdInPath = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        Loan loanInPath = loanService.getLoan(loanIdInPath);
        if(loanInPath == null) {
            ctx.status(404).json("{\"error\":\"The loan doesn't exists.\"}");
            return;
        }

        if(activeUser.getRole().equals("manager")) {
            Loan loanApproved = loanService.approveLoanById(loanIdInPath);
            if (loanApproved == null) {
                ctx.status(401).json("{\"error\":\"There was an error approving the loan.\"}");
                return;
            }
            ctx.json(loanApproved);
            return;
        } else {
            logger.warn("Unauthorized access attempt.");
            ctx.status(403).json("{\"error\":\"You don't have the right privileges.\"}");
            return;
        }
    }

    public void rejectLoan(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            ctx.status(401).json("{\"message\":\"You are logged out\"}");
            return;
        }
        // Gets the activeUser from the session
        User activeUser = (User) session.getAttribute("user");
        // Gets the id passed in the path request
        int loanIdInPath = Integer.parseInt(ctx.pathParam("id"));
        // Gets the user from the DB
        Loan loanInPath = loanService.getLoan(loanIdInPath);
        if(loanInPath == null) {
            ctx.status(404).json("{\"error\":\"The loan doesn't exists.\"}");
            return;
        }

        if(activeUser.getRole().equals("manager")) {
            Loan loanRejected = loanService.rejectLoanById(loanIdInPath);
            if (loanRejected == null) {
                ctx.status(401).json("{\"error\":\"There was an error rejecting the loan.\"}");
                return;
            }
            ctx.json(loanRejected);
            return;
        } else {
            logger.warn("Unauthorized access attempt.");
            ctx.status(403).json("{\"error\":\"You don't have the right privileges.\"}");
            return;
        }
    }
}
