package com.budgettracker.service;

import java.util.List;

import com.budgettracker.models.account.Account;
import com.budgettracker.models.user.User;
import com.budgettracker.models.wishlists.WishItems;
import com.budgettracker.repository.WishlistRepository;

public class WishlistService {
    private final WishlistRepository wishlistRepository = new WishlistRepository();

    public void addWishList(User user, String item_name, double item_price) {
        WishItems item = new WishItems(item_name, item_price);
        wishlistRepository.saveWishItem(item, user.getAccount().getAccountId());
    }

    public void showWishList(User user) {
        Account account = user.getAccount();
        System.out.println("================== Wish List ==================");

        List<WishItems> items = wishlistRepository.getWishItemsByAccountId(account.getAccountId());
        if (items.isEmpty()) {
            System.out.println("No items added yet");
        } else {
            System.out.println("Current amount of your saving: $" + account.getSavingAmount());

            for (WishItems i : items) {
                System.out.println("Item name: " + i.getItem_name());
                System.out.println("Price: $" + i.getItem_price());
                System.out.println("Saving Progress: "
                        + i.calculatePercentage(account.getSavingAmount(), i.getItem_price()) + "%");
            }
        }

        System.out.println("================================================");
    }
}