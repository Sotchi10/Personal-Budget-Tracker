package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Record {
    private String record_id;
    private String type;
    private LocalDate date;
    private List<String> activities;
    private static int qty = 0;

    public Record(String type, int amount, LocalDate date, String des) {
        
    }

    //qty is for recording the amount of user after creating, the plan of Record class is to record user activity
    //such as what user spent on + where the income comes from
    //Sample output: 
    //User---------------------------
    //User Activties = 3 times
    //Added Income: +300$ from salary
    //Added Income: +100$ from gift
    //Added Expense: -100$ on shoes
    

    //Overall amount of user activity record
    public static int record_qty() {
        return qty;
    }
    
  
    public Record() {
        qty++;
        this.record_id = "user" + qty;
        this.activities = new ArrayList<>();
    }

    public String getRecord_id(){
        return record_id;
    }

    void addIncome(double amount, String source){
        String Activity = "Added Income: +" + amount + "$ from " + source;
        activities.add(Activity);
    }

    void addExpense(double amount, String source){
        String Activity = "Added Income: -" + amount + "$ from " + source;
        activities.add(Activity);
    }

    //print all activities
    public void printactivities(){
        System.out.println("User---------------------------");
        for (String Activity: activities){
            System.out.println(Activity);
        }
    }
    
}
