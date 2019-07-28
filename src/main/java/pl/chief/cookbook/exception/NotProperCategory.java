package pl.chief.cookbook.exception;

public class NotProperCategory extends RuntimeException {
    public NotProperCategory() {
        super("Not proper category");
    }

    public NotProperCategory(String message){
        super("Not proper category: " + message);
    }
}
