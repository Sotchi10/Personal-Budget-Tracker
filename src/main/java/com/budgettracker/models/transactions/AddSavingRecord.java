package com.budgettracker.models.transactions;

import java.time.LocalDate;

public class AddSavingRecord extends Record {

    public AddSavingRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.ADD_SAVING;
    }
    @Override
    public String format() {
        // TODO Auto-generated method stub
        return "Saving  | +$" + getAmount() + " | " + getNote() + " | " + getDate();
    }
}
