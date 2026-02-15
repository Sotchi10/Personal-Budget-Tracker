package service;
import models.User;
import models.WishItems;

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
        if (amount <= 0 || amount > user.getBalance() || note.isBlank())
            throw new IllegalArgumentException("amount invalid");

        if (amount > user.getLimit() && user.getLimit() != 0)
            throw new IllegalArgumentException("You spent more than your limit");
        user.decreaseBalance(amount);
        Record record = new Record(type, date, amount, note);
        user.addRecords(record);
    }

    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems();
        item.WishItem(item_name, item_price);
        user.addToWish(item);
    }

    public void limitBudget(User user, double limit_amount) {
        user.setLimit(limit_amount);
    }

    public double showBalance(User user) {
        return user.getBalance();
    }

    public void showSavingList(User user) {
        System.out.println("================== Wish List ==================");

        for (WishItems i : user.getWishList()) {
            System.out.println("Item name: " + i.getItem_name() + "\n" + "Price: $" + i.getItem_price());
            System.out.println("Saving Progress: " + i.calculatePercentage(user.getBalance(), i.getItem_price()) + "%");
        }
        System.out.println("================================================");
    }
    

    public void showTranscript(User user) {
        System.out.println("================== Transcript ==================");
        System.out.println("You currently limited your budget: $" + user.getLimit());
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
