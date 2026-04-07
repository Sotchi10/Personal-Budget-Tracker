package com.budgettracker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.budgettracker.config.DatabaseConnection;

public class SignupGUI extends JFrame {

    private JTextField nameField, emailField, passkeyField;
    private JPasswordField passwordField;
    private JButton btnCreate;
    private JButton btnBackToLogin;
    private LoginGUI loginGUI;

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

        // Back to login button
        gbc.gridx=0; gbc.gridy=4;
        btnBackToLogin = new JButton("Back to Login");
        add(btnBackToLogin, gbc);

        btnCreate.addActionListener(e -> signup());
        btnBackToLogin.addActionListener(e -> returnToLogin());

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

        try(Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String sql = "INSERT INTO users(user_name, user_password, email, passkey) VALUES(?,?,?,?)";
            int userId;

            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, pass); // later hash passwords
                stmt.setString(3, email);
                stmt.setString(4, passkey);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Failed to create user record.");
                    }
                    userId = rs.getInt(1);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts(user_id) VALUES(?)")) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }


            conn.commit();
            JOptionPane.showMessageDialog(this, "Account created! Please login.");
            dispose(); // close signup window

        } catch(SQLException ex) {
            ex.printStackTrace();
            if ("23000".equals(ex.getSQLState())) {
                JOptionPane.showMessageDialog(this, "Could not create account. That email may already exist.");
            } else {
                JOptionPane.showMessageDialog(this, "Database error while creating account: " + ex.getMessage());
            }
        }
    }

    private void returnToLogin() {
        if (loginGUI != null) {
            loginGUI.setVisible(true);
            loginGUI.toFront();
        }
        dispose();
    }
}
