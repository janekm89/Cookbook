package pl.chief.cookbook.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.IngredientRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientServiceTest {

    private Ingredient ingredient;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Before
    public void setUp(){
        ingredient = new IngredientBuilder().withName("Ser").withUnit(MeasurementUnit.GRAM)
                .withCategory(IngredientCategory.DAIRY_AND_EGGS).createIngredient();
    }

    @Test
    public void shouldAddIngredient() throws EntityAlreadyExistException {
        ingredientService.addIngredient(ingredient);
    }

    @Test
    public void shouldFindIngredientByName() {
        ingredientRepository.findByName("Ser").ifPresent(ingredient -> assertEquals("Ser", ingredient.getName()));
    }

    @Test
    public void shouldFindIngredientsByCategory() {
        IngredientCategory ingredientCategory = IngredientCategory.DAIRY_AND_EGGS;
        List<Ingredient> ingredientList = ingredientRepository.findByIngredientCategory(ingredientCategory);
        assertTrue(ingredientList.stream().allMatch(i -> i.getIngredientCategory() == ingredientCategory));
    }

    @Test
    public void shouldFindIngredientAmountByIngredientIdAndRecipeId(){
        assertEquals(10.0, ingredientService.findIngredientAmountByIngredientIdAndRecipeId(2,2),0.1);
    }

    @Test
    public void shouldFindIngredientsByRecipe(){
        Recipe recipe = null;
        try {
            recipe = recipeService.findRecipeById("1");
        } catch (NotNumberException e) {
            e.printStackTrace();
        }
        assertEquals(2, ingredientService.findIngredientsByRecipe(recipe).get(0).getId());
    }

    @Test
    public void shouldFindUnitByIngredientName(){
        assertEquals(MeasurementUnit.GRAM, ingredientService.findUnitByIngredientName("Ser"));
    }
}