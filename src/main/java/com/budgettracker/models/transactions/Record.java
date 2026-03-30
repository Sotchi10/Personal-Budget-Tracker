package com.budgettracker.models.transactions;
import java.time.LocalDate;


public abstract class Record {
    private int record_id;
    private String note;
    private double amount;
    private LocalDate date; 
    
    //Constructors
    public Record(LocalDate date, double amount, String note) {
        if (date == null)
            throw new IllegalArgumentException("none speicifed tdateype");
        if (note.isBlank())
            throw new IllegalArgumentException("none speicifed note");
        if (amount <= 0)
            throw new IllegalArgumentException();
        this.note = note;
        this.date = date;
        this.amount = amount;
    }

    //Getters
    public abstract TransactionType getType();
    public abstract String format();

    public String getNote() {
        return note;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRecordId() {
        return record_id;
    }

    public double getAmount() {
        return amount;
    }

    //Setters
    public void setRecordId(int record_id) {
        this.record_id = record_id;
    }

}
