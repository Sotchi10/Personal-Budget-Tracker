package com.budgettracker.models.transactions;

import java.time.LocalDate;

public class IncomeRecord extends Record {

    public IncomeRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.INCOME;
    }
    @Override
    public String format() {
        // TODO Auto-generated method stub
        return "Expense  | +$" + getAmount() + " | " + getNote() + " | " + getDate();
    }

}
