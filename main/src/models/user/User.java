package models.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.wishlists.WishItems;
import models.account.Account;

public class User {
    private String name;
    private int age;
    private String email;
    private String password;
    private String passkey;
    private Account account;

    private List<WishItems> wishLists = new ArrayList<>();

    // Constructor
    public User(String name, int age, String email, String password, String passkey) {
        this.account = new Account();
        setName(name);
        setAge(age);
        //Email
        validateEmail(email);
        this.email = email;
        //Passkey
        validatePasskey(passkey);
        this.passkey = passkey;
        //Password
        // validatePassword(password);
        this.password = password;
        
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

    public Account getAccount() {
        return account;
    }

    public String getPasskey() {
        return passkey;
    }

    public String getPassword() {
        return password;
    }


    // Setters
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name is invalid");
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 18)
            throw new IllegalArgumentException("age restriction");
        this.age = age;
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("\nEmail cannot be empty\n");

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex))
            throw new IllegalArgumentException("\nInvalid email format\n");
    }

    public static void validatePasskey(String passkey) {
        if (!(passkey.length() == 4 && passkey.matches("\\d{4}"))) {
            System.out.println("Passkey must contain 4 digits");
            return;
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        // String passwordRegex = "^(?=.*[0-9])" + // Password has at least one number
        //         "(?=.*[a-z])" + // at least one lowercase letter
        //         "(?=.*[A-Z])" + // at least one uppercase letter
        //         "(?=.*[@#$%^&+=!])" + // at least one special char
        //         "(?=\\S+$)" + // no space
        //         ".{8,}$"; // at least 8 characters

        // if (!password.matches(passwordRegex))
        //     throw new IllegalArgumentException(
        //             "\nPassword must contain at least:\n" +
        //                     "- 8 characters\n" +
        //                     "- 1 uppercase\n" +
        //                     "- 1 lowercase\n" +
        //                     "- 1 number\n" +
        //                     "- 1 special character\n");
    }

    
    // Methods for wish items
    public void addToWish(WishItems item) {
        wishLists.add(item);
    }

    public List<WishItems> getWishList() {
        return Collections.unmodifiableList(wishLists);
    }


}
