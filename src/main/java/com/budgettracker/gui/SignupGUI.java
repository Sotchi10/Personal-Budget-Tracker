package com.budgettracker.GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupGUI extends JFrame {

    private JTextField nameField, emailField, passkeyField;
    private JPasswordField passwordField;
    private JButton btnCreate;
    private LoginGUI loginGUI;

    private static final String URL = "jdbc:mysql://localhost:8889/budget_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public SignupGUI(LoginGUI loginGUI) {
        this.loginGUI = loginGUI;

        setTitle("Sign Up");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Name
        gbc.gridx=0; gbc.gridy=0;
        add(new JLabel("Name:"), gbc);
        gbc.gridx=1;
        nameField = new JTextField(15);
        add(nameField, gbc);

        // Password
        gbc.gridx=0; gbc.gridy=1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx=1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Email
        gbc.gridx=0; gbc.gridy=2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx=1;
        emailField = new JTextField(15);
        add(emailField, gbc);

        // Passkey
        gbc.gridx=0; gbc.gridy=3;
        add(new JLabel("4-digit Passkey:"), gbc);
        gbc.gridx=1;
        passkeyField = new JTextField(4);
        add(passkeyField, gbc);

        // Create button
        gbc.gridx=1; gbc.gridy=4;
        btnCreate = new JButton("Create Account");
        add(btnCreate, gbc);

        btnCreate.addActionListener(e -> signup());

        setVisible(true);
    }

    private void signup() {
        String name = nameField.getText();
        String pass = new String(passwordField.getPassword());
        String email = emailField.getText();
        String passkey = passkeyField.getText();

        if(name.isEmpty() || pass.isEmpty() || email.isEmpty() || passkey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        if(passkey.length() != 4 || !passkey.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Passkey must be 4 digits!");
            return;
        }

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO users(user_name, user_password, email, passkey) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, pass); // later hash passwords
            stmt.setString(3, email);
            stmt.setString(4, passkey);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account created! Please login.");
            dispose(); // close signup window

        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account! Email may already exist.");
        }
    }
}
