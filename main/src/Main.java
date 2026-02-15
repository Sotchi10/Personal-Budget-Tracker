import java.time.LocalDate;
import java.util.Scanner;

import models.User;
import service.BudgetService;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BudgetService service = new BudgetService();

        // Create user
        System.out.println("===== CREATE USER =====");
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = new User(name, age, email, password);

        boolean running = true;

        while (running) {
            System.out.println("\n===== BUDGET MENU =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Show Transcript");
            System.out.println("4. Set Budget Limit");
            System.out.println("5. Add Saving Item");
            System.out.println("6. Show Saving List");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            LocalDate date = LocalDate.now();

            switch (choice) {
                case 1:
                    System.out.print("Enter income amount: ");
                    double incomeAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter income source: ");
                    String incomeNote = sc.nextLine();

                    service.addIncome(user, incomeAmount, "income", date, incomeNote);
                    break;

                case 2:
                    System.out.print("Enter expense amount: ");
                    double expenseAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter expense item: ");
                    String expenseItem = sc.nextLine();

                    service.addExpense(user, expenseAmount, "expense", date, expenseItem);
                    break;

                case 3:
                    service.showTranscript(user);
                    break;

                case 4:
                    System.out.print("Enter budget limit: ");
                    double limit = sc.nextDouble();
                    sc.nextLine();

                    service.limitBudget(user, limit);
                    break;

                case 5:
                    System.out.print("Enter saving item name: ");
                    String item = sc.nextLine();

                    System.out.print("Enter target amount: ");
                    double target = sc.nextDouble();
                    sc.nextLine();

                    service.addWishList(user, item, target);
                    break;

                case 6:
                    service.showSavingList(user);
                    break;

                case 7:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }

        sc.close();
    }
}
