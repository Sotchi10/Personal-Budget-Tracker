package com.budgettracker.service;

import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.user.User;
import com.budgettracker.models.transactions.*;
import com.budgettracker.models.account.Account;
import com.budgettracker.auth.*;

import java.time.LocalDate;

public class SavingService {

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

        AccountAuth auth = new AccountAuth();
        if (!auth.verifyPasskey(user, passkey)) {
            System.out.println("Wrong passkey, transaction cancelled.");
            return;
        }

        account.withdrawSaving(saving);
        Record record = new UseSavingRecord(date, saving, note);
        account.addRecord(record);
        System.out.println("Withdrawal successful: -$" + saving + " from your savings");
    }
}