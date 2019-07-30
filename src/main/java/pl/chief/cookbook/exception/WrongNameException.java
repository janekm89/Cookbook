package pl.chief.cookbook.exception;

public class WrongNameException extends RuntimeException {
    public WrongNameException(String name) {
        super("Wrong name: " + name);
    }
}
