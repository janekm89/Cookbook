package pl.chief.cookbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.chief.cookbook.exception.NotProperCategory;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.validation.IngredientValidator;

import java.util.List;

@Controller
public class IngredientController {
    @Autowired
    IngredientService ingredientService;

    public List<Ingredient> getAllIngredients() {
        return ingredientService.findAllIngredients();
    }

    public List<Ingredient> getAllIngredientsByCategory(IngredientCategory ingredientCategory) {
        return ingredientService.findIngredientsByCategory(ingredientCategory);
    }

    public List<Ingredient> getAllIngredientsByCategory(String ingredientCategory) {
        if (IngredientValidator.validIngredientCategory(ingredientCategory)) {
            return ingredientService.findIngredientsByCategory(IngredientCategory.valueOf(ingredientCategory));
        } else throw new NotProperCategory(ingredientCategory);
    }

    public Ingredient getIngredientByName(String name) {
        return ingredientService.findIngredientByName(name);
    }

    public boolean addIngredient(Ingredient ingredient) {
        return ingredientService.addIngredient(ingredient);
    }

    public boolean updateIngredient(Ingredient ingredient, int ingredientId) {
        return ingredientService.updateIngredient(ingredient, ingredientId);
    }

    public boolean deleteIngredient(Ingredient ingredient) {
        return ingredientService.deleteIngredient(ingredient);
    }
}
