package com.budgettracker.models.transactions.expense;

import java.time.LocalDate;

import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.TransactionType;

public class ExpenseRecord extends Record {
    private ExpenseCategory category;

    public ExpenseRecord(LocalDate date, double amount, ExpenseCategory category, String note) {
        super(date, amount, note);
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.EXPENSE;
    }
    @Override
    public String format() {
        // TODO Auto-generated method stub
        return "Expense : -$" + getAmount() + " | Category : " + getCategory().name() + " | Note : " + getNote() + " | Date : " + getDate();
    }
}
