package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private String name;
    private int age;
    private String email;
    private String password;
    private String passkey;
    private double balance;
    private double limit_amount;
    private double saving_amount;

    private List<Record> records = new ArrayList<>();
    
    private List<WishItems> wishLists = new ArrayList<>();

    // Constructor
    public User(String name, int age, String email, String password, String passkey) {
        setName(name);
        setAge(age);
        setEmail(email);
        setPasskey(passkey);
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

    public double getLimit() {
        return limit_amount;
    }

    public double getSaving() {
        return saving_amount;
    }


    public String getPasskey() {
        return passkey;
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
    public void setLimit(double limit_amount) {
        if (limit_amount < 0) throw new IllegalArgumentException("invalid");
        this.limit_amount = limit_amount;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is invalid");
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("password is invalid");
        this.password = password;
    }

    public void setPasskey(String passkey) {
        if (!(passkey.length() == 4 && passkey.matches("\\d{4}"))) {
            System.out.println("Passkey must contain 4 digits");
            return;
        }
        this.passkey = passkey;
    }


    //Methods for user balance
    public void increaseBalance(double amount) {
        balance += amount;
    }

    public void decreaseBalance(double amount) {
        balance -= amount;
    }

    //Methods for wish items
    public void addToWish(WishItems item) {
        wishLists.add(item);
    }

    public List<WishItems> getWishList() {
        return Collections.unmodifiableList(wishLists);
    }


    //Methods for records
    public void addRecords(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }
    
    //Method for saving budget
    public void addSaving(double amount) {
        saving_amount += amount;
    }
    public void withdrawSaving(double amount) {
        saving_amount -= amount;
    }
}
