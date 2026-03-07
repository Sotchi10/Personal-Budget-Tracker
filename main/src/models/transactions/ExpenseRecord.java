package models.transactions;

import java.time.LocalDate;

public class ExpenseRecord extends Record {
    public ExpenseRecord(LocalDate date, double amount, String note) {
        super("expense", date, amount, note);
    }
}
