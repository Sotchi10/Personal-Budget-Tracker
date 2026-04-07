package com.budgettracker.gui;

public class ExpenseIncomeEntry {
    private final String date;
    private final String description;
    private final double amount;
    private final String type;
    private final String category;

    public ExpenseIncomeEntry(String date, String description, double amount, String type) {
        this(date, description, amount, type, "-");
    }

    public ExpenseIncomeEntry(String date, String description, double amount, String type, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category == null || category.isBlank() ? "-" : category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }
}
