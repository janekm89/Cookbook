package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.RecipeRepository;

import java.util.*;
import java.util.stream.Collectors;

import static pl.chief.cookbook.util.NumberParser.parseIfIsNumber;
import static pl.chief.cookbook.validation.RecipeValidator.validateRecipeTraits;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;


    public void addRecipe(Recipe recipe) {
        if (recipeRepository.findByName(recipe.getName()).isPresent()){
            throw new EntityAlreadyExistException(recipe.getName());
        }
        else if(validateRecipeTraits(recipe.getName(), recipe.getDescription(), String.valueOf(recipe.getCalories()))){
            recipeRepository.save(recipe);
        }
    }


    public List<Recipe> findAllRecipes() {
        return new ArrayList<>(recipeRepository.findAll());
    }

    public Recipe findRecipeByName(String name) {
        return recipeRepository.findByName(name).orElseThrow(RecipeNotFoundException::new);
    }

    public List<Recipe> findRecipeByDescription(String description){
        return recipeRepository.findByDescription(description);
    }

    public Recipe findRecipeById(String id) {
        int recipeId = parseIfIsNumber(id);
        return recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
    }

    public List<Recipe> findRecipesWithCaloriesIn(String caloriesMin, String caloriesMax) {
        int calMin = parseIfIsNumber(caloriesMin);
        int calMax = parseIfIsNumber(caloriesMax);
        return new ArrayList<>(recipeRepository.findByCaloriesBetween(calMin, calMax));
    }

    public List<Recipe> findRecipesWithIngredientsDistinct(Set<Ingredient> ingredients) {
        Set<Ingredient> ingredientSet = new HashSet<>(ingredients);
        return new ArrayList<>(recipeRepository.findDistinctByIngredientsIn(ingredientSet));
    }

    public List<Recipe> findAllRecipesContainingIngredient(Ingredient ingredient) {
        return recipeRepository.findByIngredientsContaining(ingredient);
    }

    public List<Recipe> findRecipesWithIngredients(Collection<Ingredient> ingredients) {
        List<Integer> ingredientIds = ingredients.stream().map(Ingredient::getId).collect(Collectors.toList());
        return new ArrayList<>(recipeRepository.findByIngredients(ingredientIds, ingredientIds.size()));
    }


}

