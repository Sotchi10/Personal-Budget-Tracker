package models;
import java.time.LocalDate;


public class Record {
    private String record_id;
    private String type;
    private String note;
    private double amount;
    private LocalDate date;
    

    //qty is for recording the amount of user after creating, the plan of Record class is to record user activity
    //such as what user spent on + where the income comes from
    //Sample output: 
    //User---------------------------
    //User Activties = 3 times
    //Added Income: +300$ from salary
    //Added Income: +100$ from gift
    //Added Expense: -100$ on shoes
    


    public Record(String type, LocalDate date, double amount, String note) {
        setType(type);
        setDate(date);
        setAmount(amount);
        setNote(note);
    }

    
    //Getters
    public String getType() {
        return type;
    }

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
    public void setType(String type) {
        if (type.isBlank()) throw new IllegalArgumentException("none speicifed type");
        this.type = type;
    }

    public void setDate(LocalDate date) {
        if (date.equals(null)) throw new IllegalArgumentException("none speicifed tdateype");
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
