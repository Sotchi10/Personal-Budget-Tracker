package com.budgettracker.service;

import com.budgettracker.models.user.User;
import com.budgettracker.models.account.Account;

public class BalanceService {

    public boolean checkBalance(User user) {
        Account account = user.getAccount();
        if (account.getBalance() == 0) {
            System.out.println("Your balance is currently $0.");
            return true;
        }
        return false;
    }

    public double showBalance(User user) {
        Account account = user.getAccount();
        return account.getBalance();
    }
}