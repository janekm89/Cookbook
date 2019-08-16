package pl.chief.cookbook.exception;

import org.springframework.security.core.AuthenticationException;

public class UserLoginNotFoundException extends AuthenticationException {
    public UserLoginNotFoundException(String login) {
        super("User with login " + login + " not found.");
    }
}
