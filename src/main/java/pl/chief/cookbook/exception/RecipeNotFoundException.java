package pl.chief.cookbook.exception;

import java.util.List;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

    public RecipeNotFoundException(List<Integer> ingredientIds){
        super("Recipe with ingredient ids: " + ingredientIds + " not found");
    }

}
