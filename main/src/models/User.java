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

    public static void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("\nEmail cannot be empty\n");

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex))
            throw new IllegalArgumentException("\nInvalid email format\n");
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        String passwordRegex =
            "^(?=.*[0-9])" +      // Password has at least one number
            "(?=.*[a-z])" +       // at least one lowercase letter
            "(?=.*[A-Z])" +       // at least one uppercase letter
            "(?=.*[@#$%^&+=!])" + // at least one special char
            "(?=\\S+$)" +         // no space
            ".{8,}$";             // at least 8 characters

        if (!password.matches(passwordRegex))
            throw new IllegalArgumentException(
                    "\nPassword must contain at least:\n" +
                    "- 8 characters\n" +
                    "- 1 uppercase\n" +
                    "- 1 lowercase\n" +
                    "- 1 number\n" +
                    "- 1 special character\n"
            );
    }

    public void setPassword(String password) {
        validatePassword(password);
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
