package pl.chief.cookbook.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.chief.cookbook.util.NumberParser.parseIfIsNumber;

@Component
public class RecipeBuilder {
    private IngredientService ingredientService;
    private Recipe recipe;

    @Autowired
    public RecipeBuilder(IngredientService ingredientService) {
        this();
        this.ingredientService = ingredientService;
    }

    public RecipeBuilder() {
        recipe = new Recipe();
    }


    public RecipeBuilder withName(String name) {
        recipe.setName(name);
        return this;
    }

    public RecipeBuilder withDescription(String description) {
        recipe.setDescription(description);
        return this;
    }

    public RecipeBuilder withCategory(RecipeCategory recipeCategory) {
        recipe.setRecipeCategory(recipeCategory);
        return this;
    }

    public RecipeBuilder withCategory(String category) {
        switch (category.toLowerCase()) {
            case "zupa":
            case "soup":
                return this.withCategory(RecipeCategory.SOUP);
            case "ciasta i desery":
            case "cake and dessert":
                return this.withCategory(RecipeCategory.CAKE_DESSERT);
            case "danie mięsne":
            case "meat":
                return this.withCategory(RecipeCategory.MEAT);
            case "danie rybne":
            case "fish":
                return this.withCategory(RecipeCategory.FISH);
            case "danie wegetariańskie":
            case "vege":
                return this.withCategory(RecipeCategory.VEGE);
            case "dania zapiekane":
            case "baked":
                return this.withCategory(RecipeCategory.BAKED);
            case "drinki":
            case "drinks":
                return this.withCategory(RecipeCategory.DRINKS);
            case "kanapki":
            case "sandwich":
                return this.withCategory(RecipeCategory.SANDWICH);
            case "makarony":
            case "noodles":
                return this.withCategory(RecipeCategory.NOODLES);
            case "pierogi":
            case "dumplings":
                return this.withCategory(RecipeCategory.DUMPLINGS);
            case "dania orientalne":
            case "orient":
                return this.withCategory(RecipeCategory.ORIENT);
            case "owoce morza":
            case "sea food":
                return this.withCategory(RecipeCategory.SEA);
            case "pizza":
                return this.withCategory(RecipeCategory.PIZZA);
            case "przekąski":
            case "snack":
                return this.withCategory(RecipeCategory.SNACK);
            case "sosy":
            case "sauce":
                return this.withCategory(RecipeCategory.SAUCE);
            case "thermomix":
                return this.withCategory(RecipeCategory.THERMOMIX);
        }
        return this;
    }

    public RecipeBuilder withCalories(int calories) {
        recipe.setCalories(calories);
        return this;
    }
    public RecipeBuilder withCalories(String calories)  throws NotNumberException {
        return this.withCalories(parseIfIsNumber(calories));
    }

   public RecipeBuilder withIngredientAmount(Ingredient ingredient, Double amount) throws EntityAlreadyExistException {
        ingredientService.addIngredient(ingredient);
        return withIngredientAmount(ingredientService.findIngredientByName(ingredient.getName()).getId(), amount);
    }

   public RecipeBuilder withIngredientAmount(String ingredientName, Double amount) {
        return withIngredientAmount(ingredientService.findIngredientByName(ingredientName).getId(), amount);
    }

    public RecipeBuilder withIngredientAmount(int ingredientId, Double amount){
        recipe.getIngredientsAmount().put(ingredientId, amount);
        return this;
    }
    public RecipeBuilder withIngredient(Ingredient ingredient){
        recipe.getIngredients().add(ingredient);
        return this;
    }

    public Recipe createRecipe() {
        return recipe;
    }



    public RecipeBuilder withIngredientsAmountsLists(List<Integer> ingredientIds, List<Double> ingredientAmount) {
        Map<Integer, Double> ingredientsAmounts = new HashMap<>();
        for (int i = 0; i < ingredientIds.size(); i++) {
            ingredientsAmounts.put(ingredientIds.get(i), ingredientAmount.get(i));
        }
        recipe.setIngredientsAmount(ingredientsAmounts);
        return this;
    }


}
