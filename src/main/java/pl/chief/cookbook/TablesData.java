package pl.chief.cookbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.builder.RecipeBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.features.UserRole;
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
    public TablesData(RecipeService recipeService, IngredientService ingredientService, UserService userService) throws EntityAlreadyExistException, NotNumberException, RecipeNotFoundException {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userService = userService;

        Ingredient ingredient1 = new IngredientBuilder().withName("jablko")
                .withUnit("pcs")
                .withCategory(IngredientCategory.FRUITS)
                .createIngredient();
        ingredientService.addIngredient(ingredient1);
        Ingredient ingredient1FromDB = ingredientService.findIngredientByName(ingredient1.getName());


        Ingredient ingredient2 = new IngredientBuilder().withName("maka")
                .withUnit(MeasurementUnit.KILOGRAM)
                .withCategory(IngredientCategory.FLOUR)
                .createIngredient();
        ingredientService.addIngredient(ingredient2);
        Ingredient ingredient2FromDB = ingredientService.findIngredientByName(ingredient2.getName());

        Ingredient ingredient3 = new IngredientBuilder().withName("olej")
                .withUnit(MeasurementUnit.LITER)
                .withCategory(IngredientCategory.OTHER)
                .createIngredient();
        ingredientService.addIngredient(ingredient3);
        Ingredient ingredient3FromDB = ingredientService.findIngredientByName(ingredient3.getName());

        Ingredient ingredient4 = new IngredientBuilder().withName("Pieprz")
                .withUnit(MeasurementUnit.GRAM)
                .withCategory(IngredientCategory.SPICES)
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
                .createRecipe();
        recipeService.addRecipe(recipe1);
        Recipe recipe1FromDB = recipeService.findRecipeByName(recipe1.getName());

        Recipe recipe2 = new RecipeBuilder().withName("Placek")
                .withCalories(120)
                .withCategory(RecipeCategory.PANCAKES)
                .withDescription("usmazyc")
                .withIngredientAmount(ingredient2FromDB.getId(), 0.3)
                .withIngredientAmount(ingredient3FromDB.getId(), 0.11)
                .createRecipe();
        recipeService.addRecipe(recipe2);
        Recipe recipe2FromDB = recipeService.findRecipeByName(recipe2.getName());


        Recipe recipe3 = new RecipeBuilder().withName("Popieprzone")
                .withCalories(1)
                .withCategory(RecipeCategory.VEGE)
                .withDescription("pieprzyc")
                .withIngredientAmount(ingredient4FromDB.getId(), 11.0)
                .createRecipe();
        recipeService.addRecipe(recipe3);
        Recipe recipe3FromDB = recipeService.findRecipeByName(recipe3.getName());

        User user1 = new User();
        user1.setEmail("wp@wp.pl");
        user1.setName("admin");
        user1.setLogin("admin");
        user1.setPassword("admin");
        user1.setSurname("adminowski");
        user1.setUserRole(UserRole.ADMIN);
        user1.getIngredients().add(ingredient1FromDB);
        user1.getIngredients().add(ingredient2FromDB);
        user1.getIngredients().add(ingredient3FromDB);
        user1.getRecipes().add(recipe1FromDB);
        user1.getRecipes().add(recipe2FromDB);
        userService.addUser(user1);

        User user2 = new User();
        user2.setEmail("wp1@wp.pl");
        user2.setName("user");
        user2.setLogin("user");
        user2.setPassword("user");
        user2.setSurname("userski");
        user2.setUserRole(UserRole.USER);
        user2.getIngredients().add(ingredient4FromDB);
        user2.getRecipes().add(recipe3FromDB);
        userService.addUser(user2);
    }
}
