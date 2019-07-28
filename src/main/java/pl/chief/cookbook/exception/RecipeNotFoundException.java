package pl.chief.cookbook.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

}
