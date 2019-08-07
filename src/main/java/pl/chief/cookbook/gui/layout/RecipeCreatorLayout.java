package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.gui.components.BoldLabel;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;

@Getter
public class RecipeCreatorLayout extends VerticalLayout {
    private final IngredientService ingredientService;
    private IngredientSelector ingredientSelector;
    private RecipeCreatorBar recipeCreatorBar;
    private Recipe createdRecipe;


    @Autowired
    public RecipeCreatorLayout(IngredientService ingredientService) {
        this.ingredientService = ingredientService;

        BoldLabel titleLabel = new BoldLabel("Create new recipe");
        recipeCreatorBar = new RecipeCreatorBar();
        ingredientSelector = new IngredientSelector(ingredientService);

        add(titleLabel, recipeCreatorBar, ingredientSelector);
    }

    public Recipe getCreatedRecipe() {
        createdRecipe = recipeCreatorBar.getCreatedRecipe();
        createdRecipe.setIngredientsAmount(ingredientSelector.getSelectedIngredientAmount());
        return createdRecipe;
    }


}
