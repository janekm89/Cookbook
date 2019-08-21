package pl.chief.cookbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.builder.RecipeBuilder;
import pl.chief.cookbook.builder.UserBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.service.UserService;

//* this class is to be removed after implementing user
//@Component
public class TablesData {
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UserService userService;

    //@Autowired
    public TablesData(RecipeService recipeService, IngredientService ingredientService, UserService userService) throws EntityAlreadyExistException, NotNumberException {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userService = userService;

        User marcin = new UserBuilder()
                .withUsername("janekm89")
                .withName("Marcin")
                .withSurname("Jankiewicz")
                .withEmail("marcin.jankiewicz@gmail.com")
                .withPassword("pass1234")
                .withRoleAdmin()
                .activated()
                .create();
        userService.addUser(marcin);
        User user1DB = userService.findUserByUsername(marcin.getUsername());

        User dawid = new UserBuilder()
                .withUsername("Dawcio")
                .withName("Dawid")
                .withSurname("Stefański")
                .withEmail("dawcio.stefanski@gmail.com")
                .withPassword("pass1234")
                .withRoleAdmin()
                .activated()
                .create();
        userService.addUser(dawid);
        User user2DB = userService.findUserByUsername(dawid.getUsername());

        User krzysiek = new UserBuilder()
                .withUsername("Krzysiek")
                .withName("Krzysztof")
                .withSurname("Szafrański")
                .withEmail("krz.szafranski@gmail.com")
                .withPassword("pass1234")
                .withRoleAdmin()
                .activated()
                .create();
        userService.addUser(krzysiek);

        Ingredient ingredient1 = new IngredientBuilder().withName("jablko")
                .withUnit("pcs")
                .withCategory(IngredientCategory.FRUITS)
                .withUserId(user1DB.getId())
                .createIngredient();
        ingredientService.addIngredient(ingredient1);
        Ingredient ingredient1FromDB = ingredientService.findIngredientByName(ingredient1.getName());


        Ingredient ingredient2 = new IngredientBuilder().withName("maka")
                .withUnit(MeasurementUnit.KILOGRAM)
                .withCategory(IngredientCategory.FLOUR)
                .withUserId(user1DB.getId())
                .createIngredient();
        ingredientService.addIngredient(ingredient2);
        Ingredient ingredient2FromDB = ingredientService.findIngredientByName(ingredient2.getName());

        Ingredient ingredient3 = new IngredientBuilder().withName("olej")
                .withUnit(MeasurementUnit.LITER)
                .withCategory(IngredientCategory.OTHER)
                .withUserId(user1DB.getId())
                .createIngredient();
        ingredientService.addIngredient(ingredient3);
        Ingredient ingredient3FromDB = ingredientService.findIngredientByName(ingredient3.getName());

        Ingredient ingredient4 = new IngredientBuilder().withName("Pieprz")
                .withUnit(MeasurementUnit.GRAM)
                .withCategory(IngredientCategory.SPICES)
                .withUserId(user2DB.getId())
                .createIngredient();
        ingredientService.addIngredient(ingredient4);
        Ingredient ingredient4FromDB = ingredientService.findIngredientByName(ingredient4.getName());

        Recipe recipe1 = new RecipeBuilder().withName("Jablecznik")
                .withCalories(200)
                .withCategory(RecipeCategory.CAKE_DESSERT)
                .withDescription("piecz go")
                .withIngredientAmount(ingredient1FromDB.getId(), 2.0)
                .withIngredientAmount(ingredient2FromDB.getId(), 1.2)
                .withIngredientAmount(ingredient3FromDB.getId(), 0.2)
                .withUserId(user1DB.getId())
                .createRecipe();
        recipeService.addRecipe(recipe1);

        Recipe recipe2 = new RecipeBuilder().withName("Placek")
                .withCalories(120)
                .withCategory(RecipeCategory.PANCAKES)
                .withDescription("usmazyc")
                .withIngredientAmount(ingredient2FromDB.getId(), 0.3)
                .withIngredientAmount(ingredient3FromDB.getId(), 0.11)
                .withUserId(user1DB.getId())
                .createRecipe();
        recipeService.addRecipe(recipe2);


        Recipe recipe3 = new RecipeBuilder().withName("Popieprzone")
                .withCalories(1)
                .withCategory(RecipeCategory.VEGE)
                .withDescription("pieprzyc")
                .withIngredientAmount(ingredient4FromDB.getId(), 11.0)
                .withUserId(user2DB.getId())
                .createRecipe();
        recipeService.addRecipe(recipe3);
    }
}
