import java.time.LocalDate;
import java.util.Scanner;

import models.User;
import service.BudgetService;

public class Main {

    public static void main(String[] args) {
        //Declarations
        Scanner sc = new Scanner(System.in);
        BudgetService service = new BudgetService();
        User user = createUser(sc);
        boolean running = true;

        while (running) {
            //Get current date
            LocalDate date = LocalDate.now();

            //Menu
            systemMenu();

            int choice = sc.nextInt();
            sc.nextLine();

            String pass_key;
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
                    System.out.print("Enter expense item: ");
                    String expenseItem = sc.nextLine();

                    System.out.print("Enter expense amount: ");
                    double expenseAmount = sc.nextDouble();
                    sc.nextLine();

                    // modify passkey
                    System.out.print("Enter passkey to continue: ");
                    pass_key = sc.nextLine();

                    service.addExpense(user, expenseAmount, "expense", date, expenseItem, pass_key);
                    break;

                case 3:
                    System.out.print("Enter saving amount: ");
                    double savingAmount = sc.nextDouble();
                    sc.nextLine();

                    service.addSavings(user, savingAmount);
                    break;

                case 4:
                    System.out.print("Enter amount you want to use: ");
                    double useAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter passkey to continue: ");
                    pass_key = sc.nextLine();
                    service.useSavings(user, useAmount, pass_key);
                    break;
                case 5:
                    service.showTranscript(user);
                    break;

                case 6:
                    System.out.print("Enter budget limit: ");
                    double limit = sc.nextDouble();
                    sc.nextLine();

                    service.limitBudget(user, limit);
                    break;

                case 7:
                    System.out.print("Enter saving item name: ");
                    String item = sc.nextLine();

                    System.out.print("Enter target amount: ");
                    double target = sc.nextDouble();
                    sc.nextLine();

                    service.addWishList(user, item, target);
                    break;

                case 8:
                    service.showSavingList(user);
                    break;

                case 9:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }

        sc.close();
    }

    private static void systemMenu() {
        System.out.println("\n===== BUDGET MENU =====");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. Add Saving");
        System.out.println("4. Use Saving");
        System.out.println("5. Show Transcript");
        System.out.println("6. Set Budget Limit");
        System.out.println("7. Add Wish Item");
        System.out.println("8. Show Wish List");
        System.out.println("9. Exit");
        System.out.print("Choose option: ");
    }

    private static User createUser(Scanner sc) {
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

        System.out.println("Create passkey: ");
        String passkey = sc.nextLine();

        User user = new User(name, age, email, password, passkey);
        return user;
    }
}
