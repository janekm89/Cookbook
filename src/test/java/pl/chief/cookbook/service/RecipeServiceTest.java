package pl.chief.cookbook.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.builder.RecipeBuilder;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.IngredientRepository;
import pl.chief.cookbook.repository.RecipeRepository;

import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
//@IfProfileValue(name="enableTests", value="true")
@SpringBootTest
public class RecipeServiceTest {
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private Recipe recipe;
    private Recipe recipe2;
    private Recipe recipe3;
    private HashSet<Ingredient> ingredientSet;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;


    @Before
    public void setUp() {
        ingredient = new IngredientBuilder()
                .withName("Pomidor")
                .withCategory(IngredientCategory.VEGETABLES)
                .withUnit(MeasurementUnit.PCS)
                .createIngredient();
        ingredientRepository.save(ingredient);

        ingredient2 = ingredientRepository.getOne(1);

        ingredient2 = ingredientRepository.getOne(1);

        recipe = new RecipeBuilder()
                .withCategory(RecipeCategory.PIZZA)
                .withCalories(400)
                .withDescription("Italian Standard Pizza")
                .withName("Margeritta")
                .withIngredientAmount(ingredientRepository.findByName(ingredient.getName()).get().getId(), 1.0)
                .createRecipe();


        recipe2 = new RecipeBuilder()
                .withCategory(RecipeCategory.DRINKS)
                .withCalories(200)
                .withDescription("Mohito drink")
                .withName("Mohito")
                .withIngredientAmount(ingredientRepository.findByName(ingredient.getName()).get().getId(), 10.0)
                .createRecipe();

        recipe3 = new RecipeBuilder()
                .withCategory(RecipeCategory.DRINKS)
                .withCalories(200)
                .withDescription("The strongest of drinks")
                .withName("Russian drink")
                .withIngredientAmount(ingredient2, 10.0)
                .createRecipe();

        recipe3 = new RecipeBuilder()
                .withCategory(RecipeCategory.DRINKS)
                .withCalories(200)
                .withDescription("The strongest of drinks")
                .withName("Russian drink")
                .withIngredientAmount(ingredient2, 10.0)
                .createRecipe();
    }


    @Test
    public void shouldAddRecipe() {
        recipeService.addRecipe(recipe);
        recipeService.addRecipe(recipe2);
        recipeService.addRecipe(recipe3);
    }


    @Test
    public void shouldFindRecipeByName() {
        recipeRepository.findByName("Margeritta").ifPresent(recipe -> assertEquals("Margeritta", recipe.getName()));
    }

    @Test
    public void shouldFindRecipeById() {
        recipeRepository.findByName("Margeritta").ifPresent(recipe -> assertEquals(1, recipe.getId()));
    }

    @Test
    public void shouldFindRecipesWithCaloriesIn() {
        int caloriesMin = 900;
        int caloriesMax = 1500;
        List<Recipe> recipeList = recipeRepository.findByCaloriesBetween(caloriesMin, caloriesMax);
        assertTrue(recipeList.stream().allMatch(r -> r.getCalories() > caloriesMin && r.getCalories() < caloriesMax));
    }

    @Test
    public void shouldFindRecipesWithChosenCategoryOnly() {
        RecipeCategory recipeCategory = RecipeCategory.CAKE_DESSERT;
        List<Recipe> recipeList = recipeRepository.findByRecipeCategory(recipeCategory);
        assertTrue(recipeList.stream().allMatch(r -> r.getRecipeCategory() == recipeCategory));
    }

    @Test
    @Transactional
    public void shouldFindAllRecipesContainingIngredient() {
        Ingredient random = ingredientRepository.getOne(1);
        List<Recipe> recipes = recipeRepository.findByIngredientsContaining(random);
        assertTrue(recipes.stream().allMatch(r ->
                r.getIngredients().contains(random)));
    }

}