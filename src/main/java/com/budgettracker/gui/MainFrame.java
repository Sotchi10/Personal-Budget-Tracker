package com.budgettracker.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.transactions.expense.ExpenseCategory;

public class MainFrame extends JFrame {
    private static final String LOGIN_CARD = "login";
    private static final String SIGNUP_CARD = "signup";
    private static final String DASHBOARD_CARD = "dashboard";

    private CardLayout rootLayout;
    private JPanel rootPanel;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField signupNameField;
    private JTextField signupEmailField;
    private JPasswordField signupPasswordField;
    private JTextField signupPasskeyField;

    private JPanel topBarPanel;
    private JLabel categoryLabel;
    private JComboBox<ExpenseCategory> categoryComboBox;
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField limitField;
    private JComboBox<String> typeComboBox;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel totalBudgetLabel;
    private JLabel savingBalanceLabel;
    private JLabel limitBalanceLabel;
    private JLabel availableBalanceLabel;

    private ExpenseIncomeTableModel tableModel;
    private JTable table;
    private String accountPasskey;
    private double baseLimitBalance;

    public MainFrame() {
        this.accountPasskey = "";
        setTitle("Personal Budget Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        rootLayout = new CardLayout();
        rootPanel = new JPanel(rootLayout);
        rootPanel.add(buildLoginPanel(), LOGIN_CARD);
        rootPanel.add(buildSignupPanel(), SIGNUP_CARD);
        rootPanel.add(buildDashboardPanel(), DASHBOARD_CARD);
        setContentPane(rootPanel);

        showLogin();
        setVisible(true);
    }

    private JPanel buildDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
        tableModel = new ExpenseIncomeTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        topBarPanel = new JPanel(new GridBagLayout());

        dateField = new JTextField();
        descriptionField = new JTextField();
        amountField = new JTextField();
        limitField = new JTextField(10);
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense", "Add Saving", "Use Saving"});
        categoryLabel = new JLabel("Category");
        categoryComboBox = new JComboBox<>(ExpenseCategory.values());
        JButton addButton = new JButton("Add");
        JButton logoutButton = new JButton("Logout");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        addTopBarField(topBarPanel, gbc, 0, new JLabel("Date"));
        addTopBarField(topBarPanel, gbc, 1, new JLabel("Description"));
        addTopBarField(topBarPanel, gbc, 2, new JLabel("Amount"));
        addTopBarField(topBarPanel, gbc, 3, new JLabel("Type"));
        addTopBarField(topBarPanel, gbc, 4, categoryLabel);
        addTopBarField(topBarPanel, gbc, 5, new JLabel("Action"));

        gbc.gridy = 1;
        gbc.weightx = 1.0;
        addTopBarField(topBarPanel, gbc, 0, dateField);
        addTopBarField(topBarPanel, gbc, 1, descriptionField);
        addTopBarField(topBarPanel, gbc, 2, amountField);
        addTopBarField(topBarPanel, gbc, 3, typeComboBox);
        addTopBarField(topBarPanel, gbc, 4, categoryComboBox);
        addTopBarField(topBarPanel, gbc, 5, addButton);

        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        topBarPanel.add(logoutButton, gbc);

        dashboardPanel.add(topBarPanel, BorderLayout.NORTH);
        dashboardPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bottomGbc = new GridBagConstraints();
        bottomGbc.gridx = 0;
        bottomGbc.fill = GridBagConstraints.HORIZONTAL;
        bottomGbc.weightx = 1.0;
        bottomGbc.insets = new Insets(4, 4, 4, 4);

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpenseLabel = new JLabel("Total Expense: $0.00");
        totalBudgetLabel = new JLabel("Total Budget: $0.00");
        savingBalanceLabel = new JLabel("Saving Balance: $0.00");
        limitBalanceLabel = new JLabel("Limit Balance: $0.00");
        availableBalanceLabel = new JLabel("Available After Limit: $0.00");

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(totalBudgetLabel);
        summaryPanel.add(savingBalanceLabel);
        summaryPanel.add(limitBalanceLabel);
        summaryPanel.add(availableBalanceLabel);

        JPanel limitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton updateLimitButton = new JButton("Update Limit");
        limitPanel.add(new JLabel("Limit Balance"));
        limitPanel.add(limitField);
        limitPanel.add(updateLimitButton);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All");

        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        bottomGbc.gridy = 0;
        bottomPanel.add(summaryPanel, bottomGbc);
        bottomGbc.gridy = 1;
        bottomPanel.add(limitPanel, bottomGbc);
        bottomGbc.gridy = 2;
        bottomPanel.add(buttonPanel, bottomGbc);

        dashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        clearButton.addActionListener(e -> clearEntries());
        updateLimitButton.addActionListener(e -> updateLimitBalance());
        logoutButton.addActionListener(e -> logout());
        typeComboBox.addActionListener(e -> updateCategoryVisibility());

        updateCategoryVisibility();
        updateSummary();
        return dashboardPanel;
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        loginUsernameField = new JTextField(18);
        loginPasswordField = new JPasswordField(18);
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Create Account");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        panel.add(loginUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        panel.add(loginPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(signupButton, gbc);

        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> showSignup());
        return panel;
    }

    private JPanel buildSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        signupNameField = new JTextField(18);
        signupEmailField = new JTextField(18);
        signupPasswordField = new JPasswordField(18);
        signupPasskeyField = new JTextField(4);
        JButton createButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back to Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        panel.add(signupNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        panel.add(signupEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        panel.add(signupPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("4-digit Passkey"), gbc);
        gbc.gridx = 1;
        panel.add(signupPasskeyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(createButton, gbc);
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        createButton.addActionListener(e -> signup());
        backButton.addActionListener(e -> showLogin());
        return panel;
    }

    private void addTopBarField(JPanel panel, GridBagConstraints template, int column, java.awt.Component component) {
        GridBagConstraints gbc = (GridBagConstraints) template.clone();
        gbc.gridx = column;
        if (column == 1) {
            gbc.weightx = template.gridy == 1 ? 1.8 : 0;
        } else if (column == 0 || column == 2) {
            gbc.weightx = template.gridy == 1 ? 1.1 : 0;
        } else {
            gbc.weightx = template.gridy == 1 ? 1.0 : 0;
        }
        panel.add(component, gbc);
    }

    private void showLogin() {
        clearAuthFields();
        rootLayout.show(rootPanel, LOGIN_CARD);
    }

    private void showSignup() {
        clearAuthFields();
        rootLayout.show(rootPanel, SIGNUP_CARD);
    }

    private void showDashboard() {
        rootLayout.show(rootPanel, DASHBOARD_CARD);
    }

    private void login() {
        String user = loginUsernameField.getText().trim();
        String pass = new String(loginPasswordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT passkey FROM users WHERE user_name = ? AND user_password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accountPasskey = rs.getString("passkey");
                JOptionPane.showMessageDialog(this, "Login Successful!");
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void signup() {
        String name = signupNameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = new String(signupPasswordField.getPassword());
        String passkey = signupPasskeyField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passkey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        if (!passkey.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Passkey must be 4 digits!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int userId = createUserRecord(conn, name, email, password, passkey);
            createAccountRecord(conn, userId);

            conn.commit();
            JOptionPane.showMessageDialog(this, "Account created! Please login.");
            showLogin();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, buildSignupErrorMessage(ex));
        }
    }

    private int createUserRecord(Connection conn, String name, String email, String password, String passkey) throws SQLException {
        String sql = "INSERT INTO users(user_name, user_password, email, passkey) VALUES(?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, passkey);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Failed to create user record.");
    }

    private void createAccountRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO accounts(user_id) VALUES(?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private String buildSignupErrorMessage(SQLException ex) {
        if ("23000".equals(ex.getSQLState())) {
            return "Could not create account. That email may already exist.";
        }
        return "Database error while creating account: " + ex.getMessage();
    }

    private void logout() {
        accountPasskey = "";
        tableModel.clearEntries();
        baseLimitBalance = 0;
        limitField.setText("");
        updateSummary();
        showLogin();
    }

    private void clearAuthFields() {
        if (loginUsernameField != null) {
            loginUsernameField.setText("");
        }
        if (loginPasswordField != null) {
            loginPasswordField.setText("");
        }
        if (signupNameField != null) {
            signupNameField.setText("");
        }
        if (signupEmailField != null) {
            signupEmailField.setText("");
        }
        if (signupPasswordField != null) {
            signupPasswordField.setText("");
        }
        if (signupPasskeyField != null) {
            signupPasskeyField.setText("");
        }
    }

    private void updateCategoryVisibility() {
        boolean isExpense = "Expense".equals(typeComboBox.getSelectedItem());
        categoryLabel.setVisible(isExpense);
        categoryComboBox.setVisible(isExpense);
        topBarPanel.revalidate();
        topBarPanel.repaint();
    }

    private void addEntry() {
        String date = dateField.getText().trim();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String type = typeComboBox.getSelectedItem().toString();
        String category = "Expense".equals(type) ? categoryComboBox.getSelectedItem().toString() : "-";

        if (date.isEmpty() || description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            if (!validateTransaction(type, amount)) {
                return;
            }

            ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, description, amount, type, category);
            tableModel.addEntry(entry);

            updateSummary();

            dateField.setText("");
            descriptionField.setText("");
            amountField.setText("");
            categoryComboBox.setSelectedIndex(0);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
        }
    }

    private boolean validateTransaction(String type, double amount) {
        double totalBudget = calculateTotalBudget();
        double savingBalance = calculateSavingBalance();
        double limitBalance = calculateEffectiveLimitBalance();

        switch (type) {
            case "Expense":
                if (!verifyPasskey("Enter passkey to add this expense:")) {
                    return false;
                }
                if (amount > totalBudget) {
                    JOptionPane.showMessageDialog(this, "Insufficient total budget.");
                    return false;
                }
                if (totalBudget - amount < limitBalance) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Expense rejected. Total budget cannot go below the limit balance of $%.2f.", limitBalance));
                    return false;
                }
                return true;
            case "Add Saving":
                if (amount > totalBudget) {
                    JOptionPane.showMessageDialog(this, "You do not have enough total budget to move into saving.");
                    return false;
                }
                return true;
            case "Use Saving":
                if (!verifyPasskey("Enter passkey to use saving:")) {
                    return false;
                }
                if (amount > savingBalance) {
                    JOptionPane.showMessageDialog(this, "Use Saving amount cannot be more than your current saving balance.");
                    return false;
                }
                return true;
            case "Income":
            default:
                return true;
        }
    }

    private void deleteEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeEntry(selectedRow);
            updateSummary();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private void clearEntries() {
        tableModel.clearEntries();
        updateSummary();
    }

    private void updateSummary() {
        double totalIncome = calculateTotalIncome();
        double totalExpense = calculateTotalExpense();
        double totalBudget = calculateTotalBudget();
        double savingBalance = calculateSavingBalance();
        double limitBalance = calculateEffectiveLimitBalance();
        double availableAfterLimit = totalBudget - limitBalance;

        totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
        totalBudgetLabel.setText(String.format("Total Budget: $%.2f", totalBudget));
        savingBalanceLabel.setText(String.format("Saving Balance: $%.2f", savingBalance));
        limitBalanceLabel.setText(String.format("Limit Balance: $%.2f", limitBalance));
        availableBalanceLabel.setText(String.format("Available After Limit: $%.2f", availableAfterLimit));
        limitField.setText(String.format("%.2f", limitBalance));
    }

    private void updateLimitBalance() {
        String limitText = limitField.getText().trim();
        if (limitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a limit balance.");
            return;
        }

        try {
            double requestedLimit = Double.parseDouble(limitText);
            if (requestedLimit < 0) {
                JOptionPane.showMessageDialog(this, "Limit balance cannot be negative.");
                return;
            }
            if (!verifyPasskey("Enter passkey to change the limit balance:")) {
                return;
            }

            double totalUsedSaving = calculateTotalUsedSaving();
            if (requestedLimit < totalUsedSaving) {
                JOptionPane.showMessageDialog(this,
                        String.format("Limit balance cannot be less than $%.2f because Use Saving already extended it by that amount.", totalUsedSaving));
                limitField.setText(String.format("%.2f", calculateEffectiveLimitBalance()));
                return;
            }

            baseLimitBalance = requestedLimit - totalUsedSaving;
            updateSummary();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Limit balance must be a valid number.");
        }
    }

    private boolean verifyPasskey(String message) {
        if (accountPasskey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No passkey is available for this session. Please log in first.");
            return false;
        }

        String enteredPasskey = JOptionPane.showInputDialog(this, message);
        if (enteredPasskey == null) {
            return false;
        }
        if (!accountPasskey.equals(enteredPasskey.trim())) {
            JOptionPane.showMessageDialog(this, "Wrong passkey. Action cancelled.");
            return false;
        }
        return true;
    }

    private double calculateTotalIncome() {
        return calculateAmountByType("Income");
    }

    private double calculateTotalExpense() {
        return calculateAmountByType("Expense");
    }

    private double calculateTotalSaved() {
        return calculateAmountByType("Add Saving");
    }

    private double calculateTotalUsedSaving() {
        return calculateAmountByType("Use Saving");
    }

    private double calculateSavingBalance() {
        return calculateTotalSaved() - calculateTotalUsedSaving();
    }

    private double calculateTotalBudget() {
        return calculateTotalIncome() - calculateTotalExpense() - calculateTotalSaved() + calculateTotalUsedSaving();
    }

    private double calculateEffectiveLimitBalance() {
        return baseLimitBalance + calculateTotalUsedSaving();
    }

    private double calculateAmountByType(String type) {
        double total = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            ExpenseIncomeEntry entry = tableModel.getEntry(i);
            if (entry != null && type.equalsIgnoreCase(entry.getType())) {
                total += entry.getAmount();
            }
        }

        return total;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
