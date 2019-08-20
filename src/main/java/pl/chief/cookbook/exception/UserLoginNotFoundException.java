package pl.chief.cookbook.exception;

import org.springframework.security.core.AuthenticationException;

public class UserLoginNotFoundException extends AuthenticationException {
    public UserLoginNotFoundException(String username) {
        super("User with username " + username + " not found.");
    }
}
