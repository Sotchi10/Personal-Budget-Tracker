package com.budgettracker.gui;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.user.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton btnLogin, btnSignup;

    public LoginGUI() {
        setTitle("Budget Tracker Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 2;
        btnLogin = new JButton("Login");
        add(btnLogin, gbc);

        // Signup button
        gbc.gridx = 1;
        btnSignup = new JButton("Sign Up");
        add(btnSignup, gbc);

        // 🔹 Actions
        btnLogin.addActionListener(e -> login());
        btnSignup.addActionListener(e -> new SignupGUI(this));

        setVisible(true);
    }

    private void login() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT user_id, user_name, age, email, user_password, passkey FROM users WHERE user_name = ? AND user_password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass); // later hash passwords
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                User loggedInUser = buildUserFromLogin(rs);
                new MainFrame(loggedInUser);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private User buildUserFromLogin(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String name = rs.getString("user_name");
        int age = rs.getInt("age");
        if (rs.wasNull() || age < 18) {
            age = 18;
        }
        String email = rs.getString("email");
        String password = rs.getString("user_password");
        String passkey = rs.getString("passkey");

        User user = new User(name, age, email, password, passkey);
        user.setUserId(userId);
        return user;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
