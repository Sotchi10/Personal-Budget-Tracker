package com.budgettracker.models.transactions;

import java.time.LocalDate;

public class ExpenseRecord extends Record {

    public ExpenseRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.EXPENSE;
    }
    @Override
    public String format() {
        // TODO Auto-generated method stub
        return "Expense  | -$" + getAmount() + " | " + getNote() + " | " + getDate();
    }
}
