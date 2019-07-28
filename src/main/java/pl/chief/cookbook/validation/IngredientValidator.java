package pl.chief.cookbook.validation;

import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IngredientValidator {

    public static boolean validIngredientUnit(String unit) {
        List<String> measurementUnits = new ArrayList<>();
        Arrays.stream(MeasurementUnit.values()).forEach(u -> measurementUnits.add(u.toString().toLowerCase()));
        return measurementUnits.contains(unit.toLowerCase());
    }

    public static boolean validIngredientCategory(String category) {
        List<String> categoryList = new ArrayList<>();
        Arrays.stream(IngredientCategory.values()).forEach(c -> categoryList.add(c.toString().toLowerCase()));
        return categoryList.contains(category.toLowerCase());
    }

    public static boolean validIngredientAmount(Map<Integer, Double> ingredientsAmount) {
        List<Double> amounts = new ArrayList<>(ingredientsAmount.values());
        for (Double amount : amounts) {
            if (!validIngredientAmount(amount))
                return false;
        }
        return true;
    }

    public static boolean validIngredientAmount(double amount) {
        return !(amount <= 0);
    }


}
