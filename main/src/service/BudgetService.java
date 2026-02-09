package service;
import models.User;
import java.time.LocalDate;
import models.Record;


public class BudgetService {

    public void addIncome(User user, double amount, String type, LocalDate date , String note) {
        if (amount <= 0 || note.isBlank()) throw new IllegalArgumentException("amount invalid");
        user.increaseBalance(amount);
        Record record = new Record(type, date, amount, note);
        user.addRecords(record);
    }

    public void addExpense(User user, double amount, String type, LocalDate date, String note) {
        if (amount <= 0 || amount > user.getBalance() || note.isBlank()) throw new IllegalArgumentException("amount invalid");
        user.decreaseBalance(amount);
        Record record = new Record(type, date, amount, note);
        user.addRecords(record);
    }

    public double showBalance(User user) {
        return user.getBalance();
    }

    
    public void showTranscript(User user) {
        System.out.println("================== Transcript ==================");
        System.out.println("Type  | " + "Amount" +" | " + "Notes" +" | " + "Date");   
        for (Record r : user.getRecords()) {  
            if (r.getType().equalsIgnoreCase("income")) {
                System.out.println("Income  | +$" + r.getAmount() +" | " + r.getNote() +" | " + r.getDate());

            } else if (r.getType().equalsIgnoreCase("expense")) {
                System.out.println("Expense | -$" + r.getAmount() +" | " + r.getNote() +" | " + r.getDate());
            }
        }
        System.out.println("================================================");
        System.out.println("Current Balance: $" + user.getBalance());
    }

}
