package pl.chief.cookbook.exception;

import javax.validation.ValidationException;

public class NotNumberException extends ValidationException {

    public NotNumberException(String number) {
        super(number + " is not a valid number");
    }

    public NotNumberException() {
    }
}
