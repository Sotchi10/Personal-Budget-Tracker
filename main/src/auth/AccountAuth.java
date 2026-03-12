package auth;

import models.user.User;

public class AccountAuth {

    public boolean verifyPasskey(User user, String inputPasskey) {
        return user.getPasskey() != null && user.getPasskey().equals(inputPasskey);
    }

    public boolean verifyPassword(User user, String inputPassword) {
        return user.getPassword() != null && user.getPassword().equals(inputPassword);
    }

    public boolean verifyEmail(User user, String inputEmail) {
        return user.getEmail() != null && user.getEmail().equalsIgnoreCase(inputEmail);
    }
}