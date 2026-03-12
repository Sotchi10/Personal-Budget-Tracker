package models.transactions;

import java.time.LocalDate;

public class UseSavingRecord extends Record {
    public UseSavingRecord(LocalDate date, double amount, String note) {
        super(date, amount, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.USESAVING;
    }

    @Override
    public String format() {
        // TODO Auto-generated method stub
        return "Saving  | -$" + getAmount() + " | " + getNote() + " | " + getDate();
    }
}
