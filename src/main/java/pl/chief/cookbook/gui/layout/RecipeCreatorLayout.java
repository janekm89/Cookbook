package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.gui.components.BoldLabel;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;

@Getter
@Component
@Route("creator")
public class RecipeCreatorLayout extends VerticalLayout {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private IngredientSelector ingredientSelector;
    private RecipeCreatorBar recipeCreatorBar;


    @Autowired
    public RecipeCreatorLayout(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;

        BoldLabel titleLabel = new BoldLabel("Create new recipe");
        recipeCreatorBar = new RecipeCreatorBar();

        VerticalLayout addRecipeButtonLayout = buildAdRecipeButton();
        ingredientSelector = new IngredientSelector(ingredientService);

        add(titleLabel, recipeCreatorBar, ingredientSelector, addRecipeButtonLayout);
    }

    public Recipe getRecipe(){
        Recipe recipe = new Recipe();
        recipe.setName(recipeCreatorBar.getNameField().getValue());
        recipe.setDescription(recipeCreatorBar.getDescriptionField().getValue());
        recipe.setRecipeCategory(recipeCreatorBar.getRecipeCategoryComboBox().getValue());
        recipe.setCalories(recipeCreatorBar.getCaloriesField().getValue().intValue());
        recipe.setIngredientsAmount(ingredientSelector.getSelectedIngredientAmount());
        return recipe;
    }


    private VerticalLayout buildAdRecipeButton() {
        VerticalLayout addRecipeButtonLayout = new VerticalLayout();
        Button addRecipeButton = new Button("create recipe");
        addRecipeButton.addClickListener(
                buttonClickEvent -> {
                    try {
                        Recipe recipe = getRecipe();
                        recipeService.addRecipe(recipe);
                    } catch (NotNumberException e) {
                        e.printStackTrace();
                    }
                    //recipeGrid.setItems(recipeService.findAllRecipes());

                    Notification notification = new MiddleNotification("Recipe sucessfully added to database");
                    notification.open();
                });
        addRecipeButtonLayout.add(addRecipeButton);
        addRecipeButtonLayout.setAlignItems(Alignment.END);
        //addRecipeButtonLayout.setWidth("80%");

        return addRecipeButtonLayout;
    }


}
