package pl.chief.cookbook.exception;


public class NotNumberException extends Exception {

    public NotNumberException(String number) {
        super(number + " is not a valid number");
    }

    public NotNumberException() {
    }
}
