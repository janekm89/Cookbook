package pl.chief.cookbook.validation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IngredientValidator {


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
