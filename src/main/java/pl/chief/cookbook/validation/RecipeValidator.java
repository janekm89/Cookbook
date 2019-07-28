package pl.chief.cookbook.validation;

import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.features.RecipeCategory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.chief.cookbook.util.NumberParser.parseIfIsNumber;
import static pl.chief.cookbook.validation.CommonTraitsValidator.validName;

public class RecipeValidator {

    public static boolean validRecipeCategory(String category) {
        List<String> categoryList = new ArrayList<>();
        Arrays.stream(RecipeCategory.values()).forEach(c -> categoryList.add(c.toString().toLowerCase()));
        return categoryList.contains(category.toLowerCase());
    }

    public static boolean validCalories(String calories) {
        int cal = parseIfIsNumber(calories);
        return cal > 0 && cal < 10000;
    }

    public static boolean validateRecipeTraits(String recipeName, String recipeDesc, String category, String calories) {
        return validName(recipeName) && validName(recipeDesc) && validRecipeCategory(category) && validCalories(calories);
    }


}
