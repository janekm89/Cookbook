package pl.chief.cookbook.exception;

import org.springframework.security.core.AuthenticationException;

public class UserEmailNotFoundException extends AuthenticationException {
    public UserEmailNotFoundException(String email) {
        super("User with email " + email + " not found.");
    }

}
