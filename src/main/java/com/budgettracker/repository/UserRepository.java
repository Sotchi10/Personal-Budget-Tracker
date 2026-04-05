package com.budgettracker.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Scanner;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.user.User;

public class UserRepository {

    public User createUser(Scanner sc) {

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

    public void saveUser(User user) {
        String sql = "INSERT INTO users (user_name, age, email, user_password, passkey, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPasskey());
            stmt.setDate(6, java.sql.Date.valueOf(LocalDate.now()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Creating user failed, no rows affected.");
            }

            try (var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setUserId(generatedId); 
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
