package com.budgettracker;

import java.time.LocalDate;
import java.util.Scanner;

import com.budgettracker.models.transactions.expense.ExpenseCategory;
import com.budgettracker.models.user.User;
import com.budgettracker.service.BalanceService;
import com.budgettracker.service.BudgetLimitService;
import com.budgettracker.service.SavingService;
import com.budgettracker.service.TransactionService;
import com.budgettracker.service.WishlistService;

import com.budgettracker.repository.*;

public class Main {
    public static void main(String[] args) {
        // Declarations
        String pass_key;
        boolean isZero;
        Scanner sc = new Scanner(System.in);
        //Create Users
        UserRepository userRepo = new UserRepository();
        User user = userRepo.createUser(sc);
        userRepo.saveUser(user);
        AccountRepository accRepo = new AccountRepository();
        accRepo.createAccount(user);
        
        //Services
        TransactionService transactionService = new TransactionService();
        SavingService savingService = new SavingService();
        BalanceService balanceService = new BalanceService();
        BudgetLimitService budgetLimitService = new BudgetLimitService();
        WishlistService wishlistService = new WishlistService();
        boolean running = true;

        while (running) {
            // Get current date
            LocalDate date = LocalDate.now();

            // Menu
            systemMenu();

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    balanceService.showAccountInfo(user);
                    break;
                case 2:
                    System.out.print("Enter income amount: ");
                    double incomeAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter income source: ");
                    String incomeNote = sc.nextLine();

                    transactionService.addIncome(user, incomeAmount, date, incomeNote);
                    break;

                case 3:
                    isZero = balanceService.checkBalance(user);
                    if (isZero)
                        break;

                    System.out.print("Enter expense item: ");
                    String expenseItem = sc.nextLine();

                    System.out.print("Enter expense amount: ");
                    double expenseAmount = sc.nextDouble();
                    sc.nextLine();

                    ExpenseCategory expenseCategory = null;
                    while (expenseCategory == null) {
                        System.out.print("Enter Category " + java.util.Arrays.toString(ExpenseCategory.values()) + " : ");
                        String categoryInput = sc.nextLine().trim();
                        if (categoryInput.isEmpty()) {
                            System.out.println("Category cannot be empty");
                            continue;
                        }
                        try {
                            expenseCategory = ExpenseCategory.valueOf(categoryInput.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid category!! Please enter a valid category from the list");
                        }
                    }

                    // modify passkey
                    System.out.print("Enter passkey to continue: ");
                    pass_key = sc.nextLine();

                    transactionService.addExpense(user, expenseAmount, date, expenseCategory, expenseItem, pass_key);
                    break;

                case 4:
                    System.out.print("Enter saving amount: ");
                    double savingAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter saving note: ");
                    String savingNote = sc.nextLine();

                    savingService.addSavings(user, savingAmount, date, savingNote);
                    break;

                case 5:
                    isZero = savingService.checkSavings(user);
                    if (isZero)
                        break;

                    System.out.print("Enter amount you want to use: ");
                    double useAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter saving usage note: ");
                    String useNote = sc.nextLine();

                    System.out.print("Enter passkey to continue: ");
                    pass_key = sc.nextLine();
                    savingService.useSavings(user, date, useAmount, useNote, pass_key);
                    break;
                case 6:
                    transactionService.showTransaction(user);
                    break;

                case 7:
                    System.out.print("Enter budget limit: ");
                    double limit = sc.nextDouble();
                    sc.nextLine();

                    budgetLimitService.limitBudget(user, limit);
                    break;

                case 8:
                    System.out.print("Enter saving item name: ");
                    String item = sc.nextLine();

                    System.out.print("Enter target amount: ");
                    double target = sc.nextDouble();
                    sc.nextLine();

                    wishlistService.addWishList(user, item, target);
                    break;

                case 9:
                    wishlistService.showWishList(user);
                    break;

                case 10:
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
        sc.close();
    }

    // System Menu
    private static void systemMenu() {
        System.out.println("\n===== PERSONAL BUDGET TRACKER =====");
        System.out.println("1. Show Account Information");
        System.out.println("2. Log Income");
        System.out.println("3. Log Expense");
        System.out.println("4. Add to Savings");
        System.out.println("5. Use Savings");
        System.out.println("6. View Spending History");
        System.out.println("7. Set Spending Limit");
        System.out.println("8. Create Savings Goal");
        System.out.println("9. View Savings Goals");
        System.out.println("10. Exit");
        System.out.print("Choose an option: ");
    }
    
}
