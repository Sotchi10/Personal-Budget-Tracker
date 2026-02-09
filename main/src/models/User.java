package models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private int age;
    private String email;
    private String password;
    private double balance;

    private List<Record> record = new ArrayList<>(); 

    //This field is for counting the amount of time that user addincome or addexpense (used in line 73, 78)
    private int in_count = 0;
    private int out_count = 0;


    // Constructor
    public User(String name, int age, String email, String password) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPassword(password);
        this.balance = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    //Get income and expense time 
    public double getIncomeTime() {
        return in_count;
    }
    public double getExpenseTime() {
        return out_count;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is invalid");
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 18) throw new IllegalArgumentException("age restriction");
        this.age = age;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is invalid");
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password is invalid");
        this.password = password;
    }


    //Methods for user balance
    public void increaseBalance(double amount) {
        balance += amount;
    }

    public void decreaseBalance(double amount) {
        balance -= amount;
    }
}
