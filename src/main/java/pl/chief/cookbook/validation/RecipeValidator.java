package pl.chief.cookbook.validation;

import pl.chief.cookbook.exception.NotProperCalories;
import pl.chief.cookbook.exception.WrongNameException;

import static pl.chief.cookbook.util.NumberParser.parseIfIsNumber;
import static pl.chief.cookbook.validation.CommonTraitsValidator.validName;

public class RecipeValidator {


    public static boolean validCalories(String calories) {
        int cal = parseIfIsNumber(calories);
        return cal > 0 && cal < 10000;
    }

    public static boolean validateRecipeTraits(String recipeName, String recipeDesc, String calories) {
        if (!validName(recipeName)) {
            throw new WrongNameException(recipeName);
        }
        if (!validName(recipeDesc)) {
            throw new WrongNameException(recipeDesc);
        }
        if (!validCalories(calories)) {
            throw new NotProperCalories(calories);
        }
        return true;
    }


}
