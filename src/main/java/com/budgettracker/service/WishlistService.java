package com.budgettracker.service;

import com.budgettracker.models.user.User;
import com.budgettracker.models.wishlists.WishItems;
import com.budgettracker.models.account.Account;

public class WishlistService {

    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems(item_name, item_price);
        user.addToWish(item);
    }

    public void showWishList(User user) {
        Account account = user.getAccount();
        System.out.println("================== Wish List ==================");

        if (user.getWishList().isEmpty()) {
            System.out.println("No items added yet");
        } else {
            System.out.println("Current amount of your saving: $" + account.getSavingAmount());

            for (WishItems i : user.getWishList()) {
                System.out.println("Item name: " + i.getItem_name());
                System.out.println("Price: $" + i.getItem_price());
                System.out.println("Saving Progress: "
                        + i.calculatePercentage(account.getSavingAmount(), i.getItem_price()) + "%");
            }
        }

        System.out.println("================================================");
    }
}