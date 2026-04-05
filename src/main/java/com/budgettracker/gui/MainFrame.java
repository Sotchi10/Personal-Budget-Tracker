package com.budgettracker.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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

import com.budgettracker.models.account.Account;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.TransactionType;
import com.budgettracker.models.transactions.expense.ExpenseCategory;
import com.budgettracker.models.transactions.expense.ExpenseRecord;
import com.budgettracker.models.user.User;
import com.budgettracker.repository.AccountRepository;
import com.budgettracker.repository.RecordRepository;
import com.budgettracker.service.BalanceService;
import com.budgettracker.service.BudgetLimitService;
import com.budgettracker.service.SavingService;
import com.budgettracker.service.TransactionService;

public class MainFrame extends JFrame {
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
    private User currentUser;

    private final TransactionService transactionService;
    private final SavingService savingService;
    private final BalanceService balanceService;
    private final BudgetLimitService budgetLimitService;
    private final AccountRepository accountRepository;
    private final RecordRepository recordRepository;

    public MainFrame(User user) {
        this.accountPasskey = user == null ? "" : user.getPasskey();
        this.currentUser = user;
        this.transactionService = new TransactionService();
        this.savingService = new SavingService();
        this.balanceService = new BalanceService();
        this.budgetLimitService = new BudgetLimitService();
        this.accountRepository = new AccountRepository();
        this.recordRepository = new RecordRepository();
        setTitle("Personal Budget Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        setContentPane(buildDashboardPanel());

        if (currentUser != null) {
            loadUserAccountData(currentUser);
        }

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
        availableBalanceLabel = new JLabel("Remaining 'til Limit: $0.00");

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


    private void loadUserAccountData(User user) {
        Account account = accountRepository.loadAccount(user);
        List<Record> records = recordRepository.getRecordsByAccountId(account.getAccountId());
        account.clearRecords();
        for (Record record : records) {
            account.addRecord(record);
        }
        syncTableFromRecords(records);
        updateSummary();
    }

    private void logout() {
        currentUser = null;
        accountPasskey = "";
        tableModel.clearEntries();
        limitField.setText("");
        updateSummary();
        dispose();
        SwingUtilities.invokeLater(LoginGUI::new);
    }

    private void updateCategoryVisibility() {
        boolean isExpense = "Expense".equals(typeComboBox.getSelectedItem());
        categoryLabel.setVisible(isExpense);
        categoryComboBox.setVisible(isExpense);
        topBarPanel.revalidate();
        topBarPanel.repaint();
    }

    private LocalDate parseDate(String dateText) {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.");
            return null;
        }
    }

    private void appendLatestRecordToTable() {
        if (currentUser == null) {
            return;
        }
        List<Record> records = currentUser.getAccount().getRecords();
        if (records.isEmpty()) {
            return;
        }
        Record record = records.get(records.size() - 1);
        addEntryFromRecord(record);
    }

    private void syncTableFromRecords(List<Record> records) {
        tableModel.clearEntries();
        for (Record record : records) {
            addEntryFromRecord(record);
        }
    }

    private void addEntryFromRecord(Record record) {
        if (record == null) {
            return;
        }
        String typeLabel = toDisplayType(record.getType());
        String categoryLabelValue = "-";
        if (record instanceof ExpenseRecord) {
            ExpenseRecord expense = (ExpenseRecord) record;
            categoryLabelValue = expense.getCategory().name();
        }
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry(
                record.getDate().toString(),
                record.getNote(),
                record.getAmount(),
                typeLabel,
                categoryLabelValue);
        tableModel.addEntry(entry);
    }

    private String toDisplayType(TransactionType type) {
        switch (type) {
            case EXPENSE:
                return "Expense";
            case ADD_SAVING:
                return "Add Saving";
            case USE_SAVING:
                return "Use Saving";
            case INCOME:
            default:
                return "Income";
        }
    }

    private Record getRecordForRow(int rowIndex) {
        if (currentUser == null) {
            return null;
        }
        List<Record> records = currentUser.getAccount().getRecords();
        if (rowIndex < 0 || rowIndex >= records.size()) {
            return null;
        }
        return records.get(rowIndex);
    }

    private void reloadRecordsAndBalances() {
        if (currentUser == null) {
            return;
        }
        Account account = currentUser.getAccount();
        List<Record> records = recordRepository.getRecordsByAccountId(account.getAccountId());
        account.clearRecords();
        for (Record record : records) {
            account.addRecord(record);
        }


        double balance = 0;
        double saving = 0;
        for (Record record : records) {
            if (record == null) {
                continue;
            }
            switch (record.getType()) {
                case INCOME:
                    balance += record.getAmount();
                    break;
                case EXPENSE:
                    balance -= record.getAmount();
                    break;
                case ADD_SAVING:
                    balance -= record.getAmount();
                    saving += record.getAmount();
                    break;
                case USE_SAVING:
                    balance += record.getAmount();
                    saving -= record.getAmount();
                    break;
                default:
                    break;
            }
        }

        account.setBalance(balance);
        account.setSavingAmount(saving);
        accountRepository.updateBalance(currentUser, balance);
        accountRepository.updateSaving(currentUser, saving);

        syncTableFromRecords(records);
        updateSummary();
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
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            LocalDate parsedDate = parseDate(date);
            if (parsedDate == null) {
                return;
            }

            if (!validateTransaction(type, amount)) {
                return;
            }

            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in first.");
                return;
            }

            int recordCountBefore = currentUser.getAccount().getRecords().size();

            switch (type) {
                case "Income":
                    transactionService.addIncome(currentUser, amount, parsedDate, description);
                    break;
                case "Expense":
                    ExpenseCategory expenseCategory = (ExpenseCategory) categoryComboBox.getSelectedItem();
                    String expensePasskey = promptPasskey("Enter passkey to add this expense:");
                    if (expensePasskey == null) {
                        return;
                    }
                    transactionService.addExpense(currentUser, amount, parsedDate, expenseCategory, description, expensePasskey);
                    break;
                case "Add Saving":
                    savingService.addSavings(currentUser, amount, parsedDate, description);
                    break;
                case "Use Saving":
                    String savingPasskey = promptPasskey("Enter passkey to use saving:");
                    if (savingPasskey == null) {
                        return;
                    }
                    savingService.useSavings(currentUser, parsedDate, amount, description, savingPasskey);
                    break;
                default:
                    return;
            }

            if (currentUser.getAccount().getRecords().size() == recordCountBefore) {
                return;
            }

            appendLatestRecordToTable();

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
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.");
            return false;
        }

        Account account = currentUser.getAccount();
        double totalBudget = account.getBalance();
        double savingBalance = account.getSavingAmount();
        double limitBalance = account.getLimitAmount();
        double remainingTillLimit = limitBalance - calculateTotalExpense();

        switch (type) {
            case "Expense":
                if (amount > totalBudget) {
                    JOptionPane.showMessageDialog(this, "Insufficient total budget.");
                    return false;
                }
                if (limitBalance > 0 && amount > remainingTillLimit) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Expense rejected. Remaining 'til limit is $%.2f.", remainingTillLimit));
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
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.");
            return;
        }

        Record record = getRecordForRow(selectedRow);
        if (record == null) {
            JOptionPane.showMessageDialog(this, "Unable to locate the selected record.");
            return;
        }

        recordRepository.deleteRecord(record.getRecordId());
        reloadRecordsAndBalances();
    }

    private void clearEntries() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.");
            return;
        }

        recordRepository.deleteRecordsByAccountId(currentUser.getAccount().getAccountId());
        Account account = currentUser.getAccount();
        account.clearRecords();
        account.setBalance(0);
        account.setSavingAmount(0);
        accountRepository.updateBalance(currentUser, 0);
        accountRepository.updateSaving(currentUser, 0);

        tableModel.clearEntries();
        updateSummary();
    }

    private void updateSummary() {
        if (currentUser == null) {
            totalIncomeLabel.setText("Total Income: $0.00");
            totalExpenseLabel.setText("Total Expense: $0.00");
            totalBudgetLabel.setText("Total Budget: $0.00");
            savingBalanceLabel.setText("Saving Balance: $0.00");
            limitBalanceLabel.setText("Limit Balance: $0.00");
            availableBalanceLabel.setText("Remaining 'til Limit: $0.00");
            limitField.setText("");
            return;
        }

        double totalIncome = calculateTotalIncome();
        double totalExpense = calculateTotalExpense();
        double totalBudget = calculateTotalBudget();
        double savingBalance = calculateSavingBalance();
        double limitBalance = calculateEffectiveLimitBalance();
        double remainingTillLimit = limitBalance > 0 ? limitBalance - totalExpense : 0;
        if (remainingTillLimit < 0) {
            remainingTillLimit = 0;
        }


        totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
        totalBudgetLabel.setText(String.format("Total Budget: $%.2f", totalBudget));
        savingBalanceLabel.setText(String.format("Saving Balance: $%.2f", savingBalance));
        limitBalanceLabel.setText(String.format("Limit Balance: $%.2f", limitBalance));
        availableBalanceLabel.setText(String.format("Remaining 'til Limit: $%.2f", remainingTillLimit));
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
            String passkey = promptPasskey("Enter passkey to change the limit balance:");
            if (passkey == null) {
                return;
            }
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in first.");
                return;
            }

            budgetLimitService.limitBudget(currentUser, requestedLimit);
            updateSummary();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Limit balance must be a valid number.");
        }
    }

    private String promptPasskey(String message) {
        if (accountPasskey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No passkey is available for this session. Please log in first.");
            return null;
        }

        String enteredPasskey = JOptionPane.showInputDialog(this, message);
        if (enteredPasskey == null) {
            return null;
        }
        if (!accountPasskey.equals(enteredPasskey.trim())) {
            JOptionPane.showMessageDialog(this, "Wrong passkey. Action cancelled.");
            return null;
        }
        return enteredPasskey.trim();
    }

    private double calculateTotalIncome() {
        return calculateAmountByType(TransactionType.INCOME);
    }

    private double calculateTotalExpense() {
        return calculateAmountByType(TransactionType.EXPENSE);
    }

    private double calculateTotalSaved() {
        return calculateAmountByType(TransactionType.ADD_SAVING);
    }

    private double calculateTotalUsedSaving() {
        return calculateAmountByType(TransactionType.USE_SAVING);
    }

    private double calculateSavingBalance() {
        return currentUser == null ? 0 : currentUser.getAccount().getSavingAmount();
    }

    private double calculateTotalBudget() {
        return currentUser == null ? 0 : balanceService.showBalance(currentUser);
    }

    private double calculateEffectiveLimitBalance() {
        return currentUser == null ? 0 : currentUser.getAccount().getLimitAmount();
    }

    private double calculateAmountByType(TransactionType type) {
        double total = 0;

        if (currentUser == null) {
            return 0;
        }

        for (Record record : currentUser.getAccount().getRecords()) {
            if (record != null && record.getType() == type) {
                total += record.getAmount();
            }
        }
        return total;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
