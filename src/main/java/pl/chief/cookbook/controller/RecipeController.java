package pl.chief.cookbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.RecipeService;

import java.util.List;
import java.util.Set;

@Controller
public class RecipeController {
    @Autowired
    RecipeService recipeService;


    public List<Recipe> getAllRecipes() {
        return recipeService.findAllRecipes();
    }

    public Recipe getRecipeByName(String name) {
        return recipeService.findRecipeByName(name);
    }

    public Recipe getRecipeById(String id) {
        return recipeService.findRecipeById(id);
    }

    public List<Recipe> getRecipesWithCaloriesIn(String calMin, String calMax) {
        return recipeService.findRecipesWithCaloriesIn(calMin, calMax);
    }

    public List<Recipe> getRecipesWithIngredientsDistinct(Set<Ingredient> ingredientSet) {
        return recipeService.findRecipesWithIngredientsDistinct(ingredientSet);
    }

    public List<Recipe> getRecipesWithIngredients(List<Ingredient> ingredients, Long numOfIngredients){
        return recipeService.findRecipesWithIngredients(ingredients, numOfIngredients);
    }

    public List<Recipe> getRecipesWithIngredient(Ingredient ingredient) {
        return recipeService.findAllRecipesContainingIngredient(ingredient);
    }

}
