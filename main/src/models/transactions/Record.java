package models.transactions;
import java.time.LocalDate;


public abstract class Record {
    private String record_id;
    private String note;
    private double amount;
    private LocalDate date; 
    
    //Constructors
    public Record(LocalDate date, double amount, String note) {
        setDate(date);
        setAmount(amount);
        setNote(note);
    }

    //Getters
    public abstract String getType();

    public String getNote() {
        return note;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getRecord_id() {
        return record_id;
    }

    public double getAmount() {
        return amount;
    }

    //Setters
    public void setDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("none speicifed tdateype");
        this.date = date;
    }

    public void setNote(String note) {
        if (note.isBlank())
            throw new IllegalArgumentException("none speicifed note");
        this.note = note;
    }

    public void setAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException();
        this.amount = amount;
    }

}
