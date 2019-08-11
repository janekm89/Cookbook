package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.gui.components.BoldLabel;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;

import java.util.Map;

@Getter
public class RecipeCreator extends VerticalLayout {
    private final IngredientService ingredientService;
    private IngredientSelector ingredientSelector;
    private RecipeCreatorBar recipeCreatorBar;
    private Recipe createdRecipe;
    private Map<Integer, Double> selectedIngredientAmount;


    @Autowired
    public RecipeCreator(IngredientService ingredientService) {
        this.ingredientService = ingredientService;

        BoldLabel titleLabel = new BoldLabel("Create new recipe");
        recipeCreatorBar = new RecipeCreatorBar();
        ingredientSelector = new IngredientSelector(ingredientService);

        add(titleLabel, recipeCreatorBar, ingredientSelector);
    }

    @Autowired
    public RecipeCreator(IngredientService ingredientService, Recipe recipe) {
        this.ingredientService = ingredientService;

        BoldLabel titleLabel = new BoldLabel("Edit recipe");
        recipeCreatorBar = new RecipeCreatorBar(recipe);
        ingredientSelector = new IngredientSelector(ingredientService, recipe);

        add(titleLabel, recipeCreatorBar, ingredientSelector);
    }


    public Recipe getCreatedRecipe() {
        createdRecipe = recipeCreatorBar.getCreatedRecipe();
        createdRecipe.setIngredientsAmount(ingredientSelector.getSelectedIngredientAmount());

        return createdRecipe;
    }

}
