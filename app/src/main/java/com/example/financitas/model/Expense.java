package com.example.financitas.model;
public class Expense {
    private int id;
    private String motive;
    private double amount;
    private ExpenseType type;

    public Expense(int id, String motive, double amount, ExpenseType type) {
        this.id = id;
        this.motive = motive;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }
}