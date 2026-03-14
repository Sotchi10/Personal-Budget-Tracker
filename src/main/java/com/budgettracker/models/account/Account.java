package com.budgettracker.models.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.TransactionType;

public class Account {

    private double balance;
    private double savingAmount;
    private double limitAmount;

    private List<Record> records = new ArrayList<>();

    // Methods for balance
    public double getBalance() {
        return balance;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

    public double getSavingAmount() {
        return savingAmount;
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

    //Methods for balance
    public void deposit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("invalid");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("invalid");
        balance -= amount;
    }

    // Method for limit amount
    public void setLimit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("invalid");
        limitAmount = amount;
    }

    // Method for saving budget
    public void addSaving(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("invalid");
        balance -= amount;
        savingAmount += amount;
    }

    public void withdrawSaving(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("invalid");
        savingAmount -= amount;
        balance += amount;
    }

    // Method for records
    public void addRecord(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }
}