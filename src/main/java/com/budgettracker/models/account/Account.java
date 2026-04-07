package com.budgettracker.models.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.TransactionType;
import com.budgettracker.models.user.User;

public class Account {
    private int accountId;
    private User user;
    private double balance;
    private double savingAmount;
    private double limitAmount;
    private List<Record> records = new ArrayList<>();

    // ID getters and setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    // User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Balance methods
    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Invalid deposit amount");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Invalid withdraw amount");
        if (amount > balance)
            throw new IllegalArgumentException("Insufficient balance");
        balance -= amount;
    }

    // Limit methods
    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Invalid limit amount");
        limitAmount = amount;
    }

    // Saving methods
    public double getSavingAmount() {
        return savingAmount;
    }

    public void setSavingAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid saving amount");
        }
        savingAmount = amount;
    }

    public void setBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid balance amount");
        }
        balance = amount;
    }

    public void addSaving(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Invalid saving amount");
        if (amount > balance)
            throw new IllegalArgumentException("Insufficient balance to save");
        balance -= amount;
        savingAmount += amount;
    }

    public void withdrawSaving(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Invalid amount");
        if (amount > savingAmount)
            throw new IllegalArgumentException("Not enough saved amount");
        savingAmount -= amount;
        balance += amount;
    }

    // Transaction records
    public void addRecord(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public void clearRecords() {
        records.clear();
    }

    public double getTotalExpense() {
        double total = 0;
        for (Record r : records) {
            if (r.getType() == TransactionType.EXPENSE) {
                total += r.getAmount();
            }
        }

        return total;
    }

    // Account info
    public void accountInfo() {
        System.out.println("=======================================");
        System.out.println("           ACCOUNT INFORMATION         ");
        System.out.println("=======================================");
        System.out.println("Account ID      : " + accountId);
        System.out.println("User ID         : " + (user != null ? user.getUserId() : "N/A"));
        System.out.println("User Name       : " + (user != null ? user.getName() : "N/A"));
        System.out.printf("Balance         : $%.2f%n", balance);
        System.out.printf("Saving Amount   : $%.2f%n", savingAmount);
        System.out.printf("Limit Amount    : $%.2f%n", limitAmount);
        System.out.printf("Total Expense   : $%.2f%n", getTotalExpense());
        System.out.println("---------------------------------------");
        if (records.isEmpty())
            System.out.println("No transactions yet.");
        System.out.println("=======================================");
    }
}
