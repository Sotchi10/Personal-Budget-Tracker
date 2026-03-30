package com.budgettracker.service;

import com.budgettracker.models.user.User;
import com.budgettracker.repository.AccountRepository;
import com.budgettracker.models.account.Account;

public class BudgetLimitService {

    public void limitBudget(User user, double limit_amount) {
        Account account = user.getAccount();
        account.setLimit(limit_amount);
        AccountRepository accRepo = new AccountRepository();
        accRepo.updateLimit(user, limit_amount);
    }
}