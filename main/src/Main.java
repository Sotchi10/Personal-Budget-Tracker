import java.time.LocalDate;
import java.util.Scanner;

import models.User;
import service.BudgetService;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        User user = new User("Johny", 18, "johnyyespapa@gmail.com", "cocomelon123");
        BudgetService service = new BudgetService();

        LocalDate date = LocalDate.parse("2026-02-20"); //for test
        // Get income from user
        System.out.print("Enter income amount: ");
        double incomeAmount = sc.nextDouble();
        sc.nextLine(); 
        System.out.print("Enter income source: ");
        String incomeNote = sc.nextLine();
        service.addIncome(user, incomeAmount, "income", date, incomeNote);

        
        // Get expense from user
        System.out.print("Enter expense amount: ");
        double expenseAmount = sc.nextDouble();
        sc.nextLine(); 
        System.out.print("Enter expense item: ");
        String expenseItem = sc.nextLine();
        service.addExpense(user, expenseAmount, "expense", date, expenseItem);

        //Show transcript
        service.showTranscript(user);

        sc.close();
    }
}
