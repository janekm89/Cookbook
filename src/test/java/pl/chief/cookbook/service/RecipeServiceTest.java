package pl.chief.cookbook.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
    private Recipe recipe;
    private Recipe recipe2;
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

        recipe = new RecipeBuilder()
                .withCategory(RecipeCategory.PIZZA)
                .withCalories(400)
                .withDescription("Italian Standard Pizza")
                .withName("Margeritta")
                .withIngredientAmount(ingredient, 1.0)
                .createRecipe();


        recipe2 = new RecipeBuilder()
                .withCategory(RecipeCategory.DRINKS)
                .withCalories(200)
                .withDescription("Mohito drink")
                .withName("Mohito")
                .withIngredientAmount(ingredient, 10.0)
                .createRecipe();

        ingredientSet = new HashSet<>();
        if (ingredientRepository.findById(1).isPresent()
                && ingredientRepository.findById(2).isPresent()
                && ingredientRepository.findById(3).isPresent()) {

            ingredientSet.add(ingredientRepository.findById(1).get());
            ingredientSet.add(ingredientRepository.findById(2).get());
            ingredientSet.add(ingredientRepository.findById(3).get());
        } else {
            throw new NullPointerException("No ingredients with choosen id");
        }
    }


    @Test
    public void shouldAddRecipe() {
        recipeService.addRecipe(recipe);
        recipeService.addRecipe(recipe2);
    }


    @Test
    public void shouldGetRecipeByName() {
        assertEquals("Margeritta", recipe.getName());
        System.out.println(recipe.getId());
    }

    @Test
    public void shouldGetRecipeById() {
        recipeRepository.findByName("Margeritta").ifPresent(recipe -> assertEquals(1, recipe.getId()));
    }

    @Test
    public void shouldGetRecipesWithCaloriesIn() {
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
    public void getRecipesWithIngredientsDistinct() {
    }

    @Test
    public void getRecipesWithIngredients() {
    }

    @Test
    public void getRecipesContainingAllIngredients() {
    }
}