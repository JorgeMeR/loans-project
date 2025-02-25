package com.revature.loansp.model;

public class Loan {
    private int id;
    private int userId;
    private int amount;
    private String status;

    public Loan() {}

    public Loan(int id, int userId, int amount, String status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; } 

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
