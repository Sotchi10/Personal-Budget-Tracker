package models.transactions;

import java.time.LocalDate;

public class IncomeRecord extends Record {
    public IncomeRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public String getType() {
        return "income";
    }

}
