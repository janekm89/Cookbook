package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.features.RecipeCategory;
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
        if (recipeRepository.findByName(recipe.getName()).isPresent()) {
            throw new EntityAlreadyExistException(recipe.getName());
        } else if (validateRecipeTraits(recipe.getName(), recipe.getDescription(), String.valueOf(recipe.getCalories()))) {
            recipeRepository.save(recipe);
        }
    }

    public List<Recipe> findAllRecipes() {
        return new ArrayList<>(recipeRepository.findAll());
    }

    public List<Recipe> findRecipeByName(String name) {
        return recipeRepository.findByNameLike(name);
    }

    public List<Recipe> findRecipeByDescription(String description) {
        return recipeRepository.findByDescriptionLike(description);
    }

    public Recipe findRecipeById(String id) {
        int recipeId = parseIfIsNumber(id);
        return findRecipeById(recipeId);
    }
    public Recipe findRecipeById(int recipeId){
        return recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);
    }

    public List<Recipe> findByCategory(RecipeCategory recipeCategory) {
        return recipeRepository.findByRecipeCategory(recipeCategory);
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

    public List<Integer> findRecipesIdWithIngredients(Collection<Ingredient> ingredients) {
        List<Integer> ingredientIds = ingredients.stream().map(Ingredient::getId).collect(Collectors.toList());
        List<Integer> recipesIds = recipeRepository.findRecipeIdByIngredients(ingredientIds, ingredientIds.size());
        if (recipesIds.size() == 0)
            throw new RecipeNotFoundException(ingredientIds);
        return recipesIds;
    }

    public List<Recipe> findRecipesWithIds(Collection<Integer> recipesIds) {
        return recipeRepository.findRecipesByIdIn(recipesIds);
    }


}

