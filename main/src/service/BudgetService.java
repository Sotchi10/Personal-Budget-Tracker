package service;

import models.user.User;
import models.wishlists.WishItems;
import models.transactions.Record;
import models.transactions.IncomeRecord;
import models.transactions.ExpenseRecord;
import java.time.LocalDate;

public class BudgetService {

    // Income
    public void addIncome(User user, double amount, LocalDate date, String note) {
        if (amount <= 0) {
            System.out.println("Income amount must be greater than 0.");
            return;
        }
        if (note == null || note.isBlank()) {
            System.out.println("Note cannot be empty.");
            return;
        }
        user.increaseBalance(amount);
        Record record = new IncomeRecord(date, amount, note);
        user.addRecords(record);
        System.out.println("\n--You gained: $" + amount + " from " + note + "--");
    }

    public boolean checkBalance(User user) {
        if (user.getBalance() == 0) {
            System.out.println("Your balance is currently $0.");
            return true;
        }
        return false;
    }

    // Expense
    public void addExpense(User user, double amount, LocalDate date, String note, String passkey) {

        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return;
        }
        if (amount > user.getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        if (note.isBlank()) {
            System.out.println("Note cannot be empty");
            return;
        }
        if (!(passkey.equals(user.getPasskey()))) {
            System.out.println("Wrong passkey");
            return;
        }

        // Check through Record to see if user spend passed the limit
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
        Record record = new ExpenseRecord(date, amount, note);
        user.addRecords(record);
        System.out.println("\n--You spent: $" + amount + " on " + note + "--");
    }

    // Add and withdraw saving
    public void addSavings(User user, double saving) {
        if (saving > user.getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        if (saving <= 0) {
            System.out.println("Amount invalid");
            return;
        }

        user.decreaseBalance(saving);
        user.addSaving(saving);
        System.out.println("Deposit successful: +$" + saving);
    }

    public boolean checkSavings(User user) {
        if (user.getSaving() == 0) {
            System.out.println("Your savings is currently $0.");
            return true;
        }
        return false;
    }

    public void useSavings(User user, double saving, String passkey) {

        if (!(passkey.equals(user.getPasskey()))) {
            System.out.println("You cant purchase now");
            return;
        }
        if (saving > user.getSaving()) {
            System.out.println("You failed to withdraw");
            return;
        }
        if (saving <= 0) {
            System.out.println("Amount invalid");
            return;
        }

        user.withdrawSaving(saving);
        user.increaseBalance(saving);
        System.out.println("Withdrawal successful: -$" + saving + "from your savings");
    }

    // WishList
    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems(item_name, item_price);
        user.addToWish(item);
    }

    // Limit Budget
    public void limitBudget(User user, double limit_amount) {
        user.setLimit(limit_amount);
    }

    // Balance
    public double showBalance(User user) {
        return user.getBalance();
    }

    // SavingWishList
    public void showSavingList(User user) {
        System.out.println("================== Wish List ==================");
        if (user.getWishList().isEmpty()) {
            System.out.println("No items added yet");
        } else {
            System.out.println("Current amount of your saving: $" + user.getSaving());
            for (WishItems i : user.getWishList()) {
                System.out.println("Item name: " + i.getItem_name() + "\n" + "Price: $" + i.getItem_price());
                System.out.println("Saving Progress: " + i.calculatePercentage(user.getSaving(), i.getItem_price()) + "%");
            }
        }

        System.out.println("================================================");
    }

    // Transcript
    public void showTranscript(User user) {
        System.out.println("================== Transcript ==================");
        if (user.getRecords().isEmpty()) {
            System.out.println("No records yet");
        } else {
            System.out.println("Current saving: $" + user.getSaving());
            System.out.println("Current limited your budget: $" + user.getLimit());
            System.out.println("Type  | " + "Amount" + " | " + "Notes" + " | " + "Date");
            for (Record r : user.getRecords()) {
                if (r.getType().equalsIgnoreCase("income")) {
                    System.out.println("Income  | +$" + r.getAmount() + " | " + r.getNote() + " | " + r.getDate());

                } else if (r.getType().equalsIgnoreCase("expense")) {
                    System.out.println("Expense | -$" + r.getAmount() + " | " + r.getNote() + " | " + r.getDate());
                }
            }
        }
        System.out.println("================================================");
        System.out.println("Current Balance: $" + user.getBalance());
    }

}
