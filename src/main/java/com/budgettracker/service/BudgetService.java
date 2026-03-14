package com.budgettracker.service;

import com.budgettracker.models.user.User;
import com.budgettracker.models.wishlists.WishItems;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.*;

import java.time.LocalDate;

import com.budgettracker.auth.*;
import com.budgettracker.models.account.Account;

public class BudgetService {

    // Income
    public void addIncome(User user, double amount, LocalDate date, String note) {
        Account account = user.getAccount();
        if (amount <= 0) {
            System.out.println("Income amount must be greater than 0.");
            return;
        }

        account.deposit(amount);
        Record record = new IncomeRecord(date, amount, note);
        account.addRecord(record);
        System.out.println("\n--You gained: $" + amount + " from " + note + "--");
    }

    public boolean checkBalance(User user) {
        Account account = user.getAccount();
        if (account.getBalance() == 0) {
            System.out.println("Your balance is currently $0.");
            return true;
        }
        return false;
    }

    // Expense
    public void addExpense(User user, double amount, LocalDate date, String note, String passkey) {

        Account account = user.getAccount();
        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return;
        }
        if (amount > account.getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        if (note.isBlank()) {
            System.out.println("Note cannot be empty");
            return;
        }

        // Verify Passkey before purchase
        AccountAuth auth = new AccountAuth();
        if (!auth.verifyPasskey(user, passkey)) {
            System.out.println("Wrong passkey, transaction cancelled.");
            return;
        }

        // Check through Record to see if account spend passed the limit
        if (account.getLimitAmount() != 0 && account.getTotalExpense() + amount > account.getLimitAmount()) {
            System.out.println("You spent over your limit price");
            return;
        }

        account.withdraw(amount);
        Record record = new ExpenseRecord(date, amount, note);
        account.addRecord(record);
        System.out.println("\n--You spent: $" + amount + " on " + note + "--");
    }

    // Add and withdraw saving
    public void addSavings(User user, double saving, LocalDate date, String note) {
        Account account = user.getAccount();
        if (saving > account.getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }
        if (saving <= 0) {
            System.out.println("Amount invalid");
            return;
        }

        account.addSaving(saving);
        Record record = new AddSavingRecord(date, saving, note);
        account.addRecord(record);
        System.out.println("Deposit successful: +$" + saving);
    }

    public boolean checkSavings(User user) {
        Account account = user.getAccount();
        if (account.getSavingAmount() == 0) {
            System.out.println("Your savings is currently $0.");
            return true;
        }
        return false;
    }

    public void useSavings(User user, LocalDate date, double saving, String note, String passkey) {

        Account account = user.getAccount();
        if (saving > account.getSavingAmount()) {
            System.out.println("You failed to withdraw");
            return;
        }
        if (saving <= 0) {
            System.out.println("Amount invalid");
            return;
        }

        // Verify passkey before withdraw
        AccountAuth auth = new AccountAuth();
        if (!auth.verifyPasskey(user, passkey)) {
            System.out.println("Wrong passkey, transaction cancelled.");
            return;
        }

        account.withdrawSaving(saving);
        Record record = new UseSavingRecord(date, saving, note);
        account.addRecord(record);
        System.out.println("Withdrawal successful: -$" + saving + "from your savings");
    }

    // Limit Budget
    public void limitBudget(User user, double limit_amount) {
        Account account = user.getAccount();
        account.setLimit(limit_amount);
    }

    // Balance
    public double showBalance(User user) {
        Account account = user.getAccount();
        return account.getBalance();
    }

    // WishList
    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems(item_name, item_price);
        user.addToWish(item);
    }

    // SavingWishList
    public void showWishList(User user) {
        Account account = user.getAccount();
        System.out.println("================== Wish List ==================");
        if (user.getWishList().isEmpty()) {
            System.out.println("No items added yet");
        } else {
            System.out.println("Current amount of your saving: $" + account.getSavingAmount());
            for (WishItems i : user.getWishList()) {
                System.out.println("Item name: " + i.getItem_name() + "\n" + "Price: $" + i.getItem_price());
                System.out.println("Saving Progress: " + i.calculatePercentage(account.getSavingAmount(), i.getItem_price()) + "%");
            }
        }

        System.out.println("================================================");
    }

    // Transcript
    public void showTranscript(User user) {
        Account account = user.getAccount();
        System.out.println("================== Transcript ==================");
        if (account.getRecords().isEmpty()) {
            System.out.println("No records yet");
        } else {
            System.out.println("Current saving: $" + account.getSavingAmount());
            System.out.println("Current limited your budget: $" + account.getLimitAmount());
            System.out.println("Type  | " + "Amount" + " | " + "Notes" + " | " + "Date");
            for (Record r : account.getRecords()) {
                if (r.getType().equals(TransactionType.INCOME)) {
                    System.out.println(r.format());
                } else if (r.getType().equals(TransactionType.EXPENSE)) {
                    System.out.println(r.format());
                } else if (r.getType().equals(TransactionType.ADDSAVING)) {
                    System.out.println(r.format());
                } else if (r.getType().equals(TransactionType.USESAVING)) {
                    System.out.println(r.format());
                }
            }
        }
        System.out.println("================================================");
        System.out.println("Current Balance: $" + account.getBalance());
    }

}
