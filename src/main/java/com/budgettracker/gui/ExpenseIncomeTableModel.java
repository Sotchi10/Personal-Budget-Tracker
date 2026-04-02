package com.budgettracker.GUI;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ExpenseIncomeTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Date", "Description", "Amount", "Type"};

    private final List<ExpenseIncomeEntry> entries;

    public ExpenseIncomeTableModel() {
        this.entries = new ArrayList<>();
    }

    public ExpenseIncomeTableModel(List<ExpenseIncomeEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseIncomeEntry entry = entries.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return entry.getDate();
            case 1:
                return entry.getDescription();
            case 2:
                return entry.getAmount();
            case 3:
                return entry.getType();
            default:
                return null;
        }
    }

    public void addEntry(ExpenseIncomeEntry entry) {
        entries.add(entry);
        int lastRow = entries.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeEntry(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < entries.size()) {
            entries.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void clearEntries() {
        if (!entries.isEmpty()) {
            entries.clear();
            fireTableDataChanged();
        }
    }

    public ExpenseIncomeEntry getEntry(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < entries.size()) {
            return entries.get(rowIndex);
        }
        return null;
    }
}
