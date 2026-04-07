package com.budgettracker.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.transactions.Record;
import com.budgettracker.models.transactions.TransactionType;
import com.budgettracker.models.transactions.AddSavingRecord;
import com.budgettracker.models.transactions.IncomeRecord;
import com.budgettracker.models.transactions.UseSavingRecord;
import com.budgettracker.models.transactions.expense.ExpenseCategory;
import com.budgettracker.models.transactions.expense.ExpenseRecord;

public class RecordRepository {

    public void saveRecord(Record record, int accountId) {
        String sql = "INSERT INTO record (account_id, transaction_type, amount, note, record_date, category) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            
            stmt.setInt(1, accountId);
            stmt.setString(2, record.getType().name());
            stmt.setDouble(3, record.getAmount());
            stmt.setString(4, record.getNote());
            stmt.setDate(5, java.sql.Date.valueOf(record.getDate()));
            if (record instanceof ExpenseRecord) {
                ExpenseRecord expense = (ExpenseRecord) record;
                stmt.setString(6, expense.getCategory().name());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }
   
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating account failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    record.setRecordId(generatedId);
                    
                }
            }

            System.out.println("Record saved to DB!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Record> getRecordsByAccountId(int accountId) {
        String sql = "SELECT record_id, transaction_type, amount, note, record_date, category FROM record WHERE account_id = ? ORDER BY record_id";
        List<Record> records = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String typeRaw = rs.getString("transaction_type");
                    TransactionType type = TransactionType.valueOf(typeRaw);
                    double amount = rs.getDouble("amount");
                    String note = rs.getString("note");
                    LocalDate date = rs.getDate("record_date").toLocalDate();

                    Record record;
                    switch (type) {
                        case EXPENSE:
                            String categoryRaw = rs.getString("category");
                            ExpenseCategory category = categoryRaw == null
                                    ? ExpenseCategory.OTHER
                                    : ExpenseCategory.valueOf(categoryRaw);
                            record = new ExpenseRecord(date, amount, category, note);
                            break;
                        case ADD_SAVING:
                            record = new AddSavingRecord(date, amount, note);
                            break;
                        case USE_SAVING:
                            record = new UseSavingRecord(date, amount, note);
                            break;
                        case INCOME:
                        default:
                            record = new IncomeRecord(date, amount, note);
                            break;
                    }

                    record.setRecordId(rs.getInt("record_id"));
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    public void deleteRecord(int recordId) {
        String sql = "DELETE FROM record WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRecordsByAccountId(int accountId) {
        String sql = "DELETE FROM record WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
