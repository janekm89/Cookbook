package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.gui.layout.RecipeCreatorBar;
import pl.chief.cookbook.gui.layout.RecipeCreatorLayout;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;


public class RecipeCreatorDialog extends Dialog {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private Recipe recipe;

    @Autowired
    public RecipeCreatorDialog(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        RecipeCreatorLayout recipeCreatorLayout = new RecipeCreatorLayout(recipeService, ingredientService);

        add(recipeCreatorLayout, buildCloseButton());

    }



    private Button buildCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.addClickListener(event -> this.close());
        return closeButton;
    }


}
