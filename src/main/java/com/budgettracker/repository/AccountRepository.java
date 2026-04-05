package com.budgettracker.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.account.Account;
import com.budgettracker.models.user.User;

public class AccountRepository {

    public void createAccount(User user) {
        String sql = "INSERT INTO accounts (user_id) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, user.getUserId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating account failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);

                    user.getAccount().setAccountId(generatedId);
                    user.getAccount().setUser(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account loadAccount(User user) {
        String sql = "SELECT account_id, balance, saving_balance, limit_balance FROM accounts WHERE user_id = ?";
        Account account = user.getAccount();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getUserId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    account.setAccountId(rs.getInt("account_id"));
                    account.setBalance(rs.getDouble("balance"));
                    account.setSavingAmount(rs.getDouble("saving_balance"));
                    account.setLimit(rs.getDouble("limit_balance"));
                    account.setUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    public void updateBalance(User user, double balance) {
        String sql = "UPDATE Accounts SET balance = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, balance);
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSaving(User user, double savingAmount) {
        String sql = "UPDATE Accounts SET saving_balance = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, savingAmount);
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLimit(User user, double limitAmount) {
        String sql = "UPDATE Accounts SET limit_balance = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, limitAmount);
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
