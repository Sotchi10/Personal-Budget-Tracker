package com.budgettracker.service;

import java.time.LocalDate;

import com.budgettracker.auth.AccountAuth;
import com.budgettracker.models.account.Account;
import com.budgettracker.models.transactions.IncomeRecord;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.expense.ExpenseCategory;
import com.budgettracker.models.transactions.expense.ExpenseRecord;
import com.budgettracker.models.user.User;

public class TransactionService {

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

    // Expense
    public void addExpense(User user, double amount, LocalDate date, ExpenseCategory category, String note, String passkey) {

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
        if (category == null) {
            System.out.println("Category cannot be empty");
            return;
        }

        AccountAuth auth = new AccountAuth();
        if (!auth.verifyPasskey(user, passkey)) {
            System.out.println("Wrong passkey, transaction cancelled.");
            return;
        }

        if (account.getLimitAmount() != 0 && account.getTotalExpense() + amount > account.getLimitAmount()) {
            System.out.println("You spent over your limit price");
            return;
        }

        account.withdraw(amount);
        Record record = new ExpenseRecord(date, amount, category, note);
        account.addRecord(record);
        System.out.println("\n--You spent: $" + amount + " on " + note + "--");
    }

    // Transcript
    public void showTransaction(User user) {
        Account account = user.getAccount();
        System.out.println("================== Transaction ==================");
        if (account.getRecords().isEmpty()) {
            System.out.println("No records yet");
        } else {
            System.out.println("Current saving: $" + account.getSavingAmount());
            System.out.println("Current limited your budget: $" + account.getLimitAmount());
            System.out.println("Type  | Amount | Notes | Date");

            for (Record r : account.getRecords()) {
                System.out.println(r.format());
            }
        }
        System.out.println("================================================");
        System.out.println("Current Balance: $" + account.getBalance());
    }
}
