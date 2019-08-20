package pl.chief.cookbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.builder.RecipeBuilder;
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
@Component
public class TablesData {
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UserService userService;

    @Autowired
    public TablesData(RecipeService recipeService, IngredientService ingredientService, UserService userService) throws EntityAlreadyExistException, NotNumberException {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userService = userService;

        User user1 = new User();
        user1.setEmail("wp@wp.pl");
        user1.setName("admin");
        user1.setUsername("admin");
        user1.setPassword("admin1234");
        user1.setSurname("adminowski");
        user1.setRole("ADMIN");
        user1.setActive(1);
        userService.addUser(user1);
        User user1DB = userService.findUserByUsername(user1.getUsername());

        User user2 = new User();
        user2.setEmail("wp1@wp.pl");
        user2.setName("user");
        user2.setUsername("user");
        user2.setPassword("user1234");
        user2.setSurname("userski");
        user2.setRole("USER");
        user2.setActive(1);
        userService.addUser(user2);
        User user2DB = userService.findUserByUsername(user2.getUsername());

        User user3 = new User();
        user3.setEmail("MJN@wp.pl");
        user3.setName("Marcin");
        user3.setUsername("janekm89");
        user3.setPassword("password");
        user3.setSurname("Jankiewicz");
        user3.setRole("ADMIN");
        user3.setActive(1);
        userService.addUser(user3);

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
