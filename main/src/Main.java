import java.util.Scanner;

import models.User;
import service.BudgetService;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        User user = new User("Johny", 18, "johnyyespapa@gmail.com", "cocomelon123");
        BudgetService service = new BudgetService();

        // Get income from user
        System.out.print("Enter income amount: ");
        double incomeAmount = sc.nextDouble();
        sc.nextLine(); // consume newline
        System.out.print("Enter income source: ");
        String incomeSource = sc.nextLine();
        service.addIncome(user, incomeAmount, incomeSource);

        
        
        // Get expense from user
        System.out.print("Enter expense amount: ");
        double expenseAmount = sc.nextDouble();
        sc.nextLine(); // consume newline
        System.out.print("Enter expense item: ");
        String expenseItem = sc.nextLine();
        service.addExpense(user, expenseAmount, expenseItem);

        // Show results
        System.out.println("\nIncome: " + incomeAmount + " from " + incomeSource);
        System.out.println("Outcome: " + expenseAmount + " for " + expenseItem);
        System.out.println("Your current balance: $" + service.showBalance(user));

        sc.close();
    }
}
