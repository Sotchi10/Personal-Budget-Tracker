import java.time.LocalDate;
import java.util.Scanner;
import models.user.User;
import service.BudgetService;

public class Main {

    public static void main(String[] args) {
        //Declarations
        String pass_key;
        boolean isZero;
        Scanner sc = new Scanner(System.in);
        User user = createUser(sc);
        BudgetService service = new BudgetService();
        boolean running = true;

        while (running) {
            //Get current date
            LocalDate date = LocalDate.now();

            //Menu
            systemMenu();

            int choice = sc.nextInt();
            sc.nextLine();

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
                    isZero = service.checkBalance(user);
                    if (isZero)
                        break;

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
                    isZero = service.checkSavings(user);
                    if (isZero)
                        break;

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

                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
        sc.close();
    }

    //System Menu
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

    //Create user input
    private static User createUser(Scanner sc) {

        System.out.println("===== CREATE USER =====");

        String name;
        int age;
        String email;
        String password;
        String passkey;

        // NAME
        while (true) {
            try {
                System.out.print("Enter name: ");
                name = sc.nextLine();
                if (name == null || name.isBlank())
                    throw new IllegalArgumentException("Name cannot be empty.");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // AGE
        while (true) {
            try {
                System.out.print("Enter age: ");
                age = Integer.parseInt(sc.nextLine());
                if (age < 18)
                    throw new IllegalArgumentException("Age must be at least 18.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Age must be a number.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // EMAIL
        while (true) {
            try {
                System.out.print("Enter email: ");
                email = sc.nextLine();
                User.validateEmail(email);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // PASSWORD
        while (true) {
            try {
                System.out.print("Enter password: ");
                password = sc.nextLine();
                User.validatePassword(password);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // PASSKEY
        while (true) {
            try {
                System.out.print("Create passkey (4 digits): ");
                passkey = sc.nextLine();
                if (!passkey.matches("\\d{4}"))
                    throw new IllegalArgumentException("Passkey must be exactly 4 digits.");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\n===== Account is successfully created =====\n");
        return new User(name, age, email, password, passkey);
    }

}
