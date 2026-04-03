package com.budgettracker.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel balanceLabel;

    private ExpenseIncomeTableModel tableModel;
    private JTable table;

    public MainFrame() {
        setTitle("Personal Budget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tableModel = new ExpenseIncomeTableModel();
        table = new JTable(tableModel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));

        dateField = new JTextField();
        descriptionField = new JTextField();
        amountField = new JTextField();
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});

        inputPanel.add(new JLabel("Date"));
        inputPanel.add(new JLabel("Description"));
        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(new JLabel("Type"));
        inputPanel.add(new JLabel("Action"));

        inputPanel.add(dateField);
        inputPanel.add(descriptionField);
        inputPanel.add(amountField);
        inputPanel.add(typeComboBox);

        JButton addButton = new JButton("Add");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalExpenseLabel = new JLabel("Total Expense: $0.00");
        balanceLabel = new JLabel("Balance: $0.00");

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(balanceLabel);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All");

        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        bottomPanel.add(summaryPanel);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        clearButton.addActionListener(e -> clearEntries());

        setVisible(true);
    }

    private void addEntry() {
        String date = dateField.getText().trim();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String type = typeComboBox.getSelectedItem().toString();

        if (date.isEmpty() || description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, description, amount, type);
            tableModel.addEntry(entry);

            updateSummary();

            dateField.setText("");
            descriptionField.setText("");
            amountField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
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
        double totalIncome = 0;
        double totalExpense = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            ExpenseIncomeEntry entry = tableModel.getEntry(i);
            if (entry != null) {
                if (entry.getType().equalsIgnoreCase("Income")) {
                    totalIncome += entry.getAmount();
                } else if (entry.getType().equalsIgnoreCase("Expense")) {
                    totalExpense += entry.getAmount();
                }
            }
        }

        double balance = totalIncome - totalExpense;

        totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
