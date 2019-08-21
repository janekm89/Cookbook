package pl.chief.cookbook.gui.security;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.chief.cookbook.model.User;

public class UserAccess {
    private static int userId;

    public static int loggedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            userId = ((User)principal).getId();
            return userId;
        }
        return -1;
    }
}
