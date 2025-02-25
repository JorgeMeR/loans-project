package com.revature.loansp.model;

public class Loan {
    private int id;
    private int userId;
    private int amount;
    private float interest;
    private String status;

    public Loan() {}

    public Loan(int id, int userId, int amount, float interest, String status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.interest = interest;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public float getInterest() { return interest; }
    public void setInterest(float interest) { this.interest = interest; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", amount='" + amount + '\'' +
                ", interest='" + interest + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return this.id == loan.id && this.userId == loan. userId && this.amount == loan.amount && this.status.equals(loan.status);
    }
}
