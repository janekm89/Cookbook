package pl.chief.cookbook.validation;

import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.validation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecipeControllerValidator {

    public static boolean validIngredientUnit(String unit) {
        List<String> measurementUnits = new ArrayList<>();
        Arrays.stream(MeasurementUnit.values()).forEach(u -> measurementUnits.add(u.toString().toLowerCase()));
        return measurementUnits.contains(unit.toLowerCase());
    }

    public static boolean validName(@NotNull @Valid String name) {
        return name.length() > 0;
    }

    public static boolean validRecipeCategory(String category) {
        List<String> categoryList = new ArrayList<>();
        Arrays.stream(RecipeCategory.values()).forEach(c -> categoryList.add(c.toString().toLowerCase()));
        return categoryList.contains(category.toLowerCase());
    }

    public static boolean validIngredientCategory(String category) {
        List<String> categoryList = new ArrayList<>();
        Arrays.stream(IngredientCategory.values()).forEach(c -> categoryList.add(c.toString().toLowerCase()));
        return categoryList.contains(category.toLowerCase());
    }

    public static boolean validCalories(String calories) {
        int cal = Integer.parseInt(calories);
        return cal > 0 && cal < 10000;
    }

    public static boolean validIngredientAmount(String amount) {
        double amountInDouble = Double.parseDouble(amount);
        return !(amountInDouble <= 0);
    }

    public static boolean validIngredientAmount(Map<Integer, Double> ingredientsAmount) {
        List<Double> amounts = new ArrayList<>(ingredientsAmount.values());
        for (Double a : amounts) {
            if (!validIngredientAmount(Double.toString(a)))
                return false;
        }
        return true;
    }

    public static boolean validateRecipeTraits(String recipeName, String recipeDesc, String category, String calories) {
        return validName(recipeName) && validName(recipeDesc) && validRecipeCategory(category) && validCalories(calories);
    }

    public static boolean validIfIsNumber(String number) {
        for (char c : number.toCharArray()) {
            if (String.valueOf(c).matches("\\D"))
                return false;
        }
        return true;
    }
}
