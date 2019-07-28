package pl.chief.cookbook.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.repository.IngredientRepository;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientServiceTest {

    private Ingredient ingredient;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Before
    public void setUp() throws Exception {
        ingredient = new IngredientBuilder().withName("Ser").withUnit(MeasurementUnit.GRAM)
                .withCategory(IngredientCategory.DAIRY_AND_EGGS).createIngredient();
    }


    @Test
    public void shouldAddIngredient() {
        ingredientService.addIngredient(ingredient);
    }


    @Test
    public void findIngredientByName() {
    }

    @Test
    public void findIngredientsByCategory() {
    }
}