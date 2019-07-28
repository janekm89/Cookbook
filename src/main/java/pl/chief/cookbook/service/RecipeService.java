package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.RecipeRepository;

import java.util.*;

import static pl.chief.cookbook.util.NumberParser.parseIfIsNumber;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;


    public boolean addRecipe(Recipe recipe) {
        if (recipeRepository.findByName(recipe.getName()).isPresent())
            return false;
        else {
            recipeRepository.save(recipe);
            return true;
        }
    }


    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipeRepository.findAll());
    }

    public Recipe getRecipeByName(String name) {
        return recipeRepository.findByName(name).orElseThrow(RecipeNotFoundException::new);
    }

    public Recipe getRecipeById(String id) {
        int recipeId = parseIfIsNumber(id);
        return recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
    }

    public List<Recipe> getRecipesWithCaloriesIn(String caloriesMin, String caloriesMax) {
        int calMin = parseIfIsNumber(caloriesMin);
        int calMax = parseIfIsNumber(caloriesMax);
        return new ArrayList<>(recipeRepository.findByCaloriesBetween(calMin, calMax));
    }

    public List<Recipe> getRecipesWithIngredientsDistinct(Set<Ingredient> ingredients) {
        Set<Ingredient> ingredientSet = new HashSet<>(ingredients);
        return new ArrayList<>(recipeRepository.findDistinctByIngredientsIn(ingredientSet));
    }

    public List<Recipe> getRecipesWithIngredients(Collection<Ingredient> ingredients) {
        Collection<Ingredient> ingredientCollection = new ArrayList<>(ingredients);
        return new ArrayList<>(recipeRepository.findByIngredients(ingredientCollection, ((long) ingredients.size())));
    }

    public List<Recipe> getRecipesContainingAllIngredients(Ingredient ingredient) {
        return recipeRepository.findByIngredientsContaining(ingredient);
    }


}

