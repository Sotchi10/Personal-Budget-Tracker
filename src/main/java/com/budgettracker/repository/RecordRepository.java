package com.budgettracker.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.transactions.Record;
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
}