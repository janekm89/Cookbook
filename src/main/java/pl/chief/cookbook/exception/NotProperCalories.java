package pl.chief.cookbook.exception;

public class NotProperCalories extends RuntimeException {
    public NotProperCalories(String calories) {
        super("Not proper value of calories: " + calories);
    }
}
