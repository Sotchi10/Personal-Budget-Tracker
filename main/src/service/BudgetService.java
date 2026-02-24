package service;
import models.user.User;
import models.wishlist.WishItems;

import java.time.LocalDate;
import models.transactions.Record;


public class BudgetService {


    //Income
    public void addIncome(User user, double amount, String type, LocalDate date, String note) {
        if (amount <= 0 || note.isBlank())
            throw new IllegalArgumentException("amount invalid");
        user.increaseBalance(amount);
        Record record = new Record(type, date, amount, note);
        user.addRecords(record);
    }

    

    //Expense
    public void addExpense(User user, double amount, String type, LocalDate date, String note, String passkey) {
        if (!(passkey.matches(user.getPasskey()))) {
            System.out.println("Wrong passkey");
            return;
        }

        if (amount <= 0 || amount > user.getBalance() || note.isBlank())
            throw new IllegalArgumentException("amount invalid");

        //Check through Record to see if user spend passed the limit
        double countAmount = amount;
        for (Record r : user.getRecords()) {
            if (r.getType().equalsIgnoreCase("expense")) {
                countAmount += r.getAmount();
            }
        }
        if (countAmount > user.getLimit() && user.getLimit() != 0) {
            System.out.println("You spent over you limit price");
            return;
        }

        user.decreaseBalance(amount);
        Record record = new Record(type, date, amount, note);
        user.addRecords(record);
    }


    //Add and withdraw saving
    public void addSavings(User user, double saving) {
        if (saving > user.getBalance()) {
            System.out.println("You dont have enough balance!");
            return;
        }
        if (saving <= 0)
            throw new IllegalArgumentException("Amount invalid");
        user.addSaving(saving);
        user.decreaseBalance(saving);
    }

    public void useSavings(User user, double saving, String passkey) {
        if (!(passkey.matches(user.getPasskey()))) {
            System.out.println("You cant purchase now");
            return;
        }
        if (saving > user.getSaving()) {
            System.out.println("You failed to withdraw");
            return;
        }
        if (saving <= 0)
            throw new IllegalArgumentException("Amount invalid");
        user.withdrawSaving(saving);
        user.increaseBalance(saving);
    }
    
    //WishList
    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems(item_name, item_price);
        user.addToWish(item);
    }


    //Limit Budget
    public void limitBudget(User user, double limit_amount) {
        user.setLimit(limit_amount);
    }


    //Balance
    public double showBalance(User user) {
        return user.getBalance();
    }



    //SavingWishList
    public void showSavingList(User user) {
        System.out.println("================== Wish List ==================");
        System.out.println("Current amount of your saving: $" + user.getSaving());
        for (WishItems i : user.getWishList()) {
            System.out.println("Item name: " + i.getItem_name() + "\n" + "Price: $" + i.getItem_price());
            System.out.println("Saving Progress: " + i.calculatePercentage(user.getSaving(), i.getItem_price()) + "%");
        }
        System.out.println("================================================");
    }
    

    //Transcript
    public void showTranscript(User user) {
        System.out.println("================== Transcript ==================");
        System.out.println("Current saving: $" + user.getSaving());
        System.out.println("Current limited your budget: $" + user.getLimit());
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
