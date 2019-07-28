package pl.chief.cookbook.exception;


public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException() {super("Ingredient not found");
    }
}