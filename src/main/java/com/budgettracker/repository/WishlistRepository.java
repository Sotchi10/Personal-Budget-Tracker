package com.budgettracker.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.budgettracker.config.DatabaseConnection;
import com.budgettracker.models.wishlists.WishItems;

public class WishlistRepository {
    public void saveWishItem(WishItems item, int accountId) {
        String sql = "INSERT INTO wishlists (account_id, item_name, item_price, saved_amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, accountId);
            stmt.setString(2, item.getItem_name());
            stmt.setDouble(3, item.getItem_price());
            stmt.setDouble(4, 0.0);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating wish item failed, no rows affected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<WishItems> getWishItemsByAccountId(int accountId) {
        String sql = "SELECT item_name, item_price FROM wishlists WHERE account_id = ? ORDER BY item_id";
        List<WishItems> items = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("item_name");
                    double price = rs.getDouble("item_price");
                    items.add(new WishItems(name, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
