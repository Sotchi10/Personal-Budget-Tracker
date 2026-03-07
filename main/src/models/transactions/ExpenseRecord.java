package models.transactions;

import java.time.LocalDate;

public class ExpenseRecord extends Record {
    public ExpenseRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public String getType() {
        return "expense";
    }
}
