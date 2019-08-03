package pl.chief.cookbook.exception;

import java.util.List;

public class RecipeNotFoundException extends Exception {

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

    public RecipeNotFoundException(List<String> ingredientIds){
        super("Recipe with ingredient names: " + ingredientIds + " not found");
    }

}
