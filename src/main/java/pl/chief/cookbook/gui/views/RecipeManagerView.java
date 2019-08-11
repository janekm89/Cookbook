package pl.chief.cookbook.gui.views;


import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.gui.components.BoldLabel;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.gui.layout.RecipeCreator;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;


@Route("recipe-manager")
public class RecipeManagerView extends VerticalLayout {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private Grid<Recipe> recipeGrid;
    private RecipeCreator recipeCreator;
    private Dialog dialog;

    @Autowired
    RecipeManagerView(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;

        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout appContent = new VerticalLayout();

        VerticalLayout recipeCreatorButton = new VerticalLayout();
        Button createButton = buildCreateButton();
        recipeCreatorButton.add(createButton);
        recipeCreatorButton.setHorizontalComponentAlignment(Alignment.END, createButton);

        Label recipeLabel = new BoldLabel("List of recipes:");
        this.recipeGrid = buildAllRecipeGrid();
        appContent.add(recipeLabel, recipeGrid, new Hr(), recipeCreatorButton);
        appLayout.setContent(appContent);

        add(appLayout);
    }

    private Grid<Recipe> buildAllRecipeGrid() {
        recipeGrid = new Grid<>(Recipe.class);
        reloadGrid();
        recipeGrid.setColumns("name", "description", "calories", "recipeCategory");
        recipeGrid.getColumnByKey("calories").setHeader("kcal");
        recipeGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("remove");
        recipeGrid.addItemClickListener(click -> {
            Recipe recipe = click.getItem();
            dialog = buildCreatorDialog(recipe);
            dialog.open();
        });
        return recipeGrid;
    }

    private void reloadGrid() {
        recipeGrid.setItems(recipeService.findAllRecipes());
    }

    private Button buildDeleteButton(Recipe recipe) {
        Button button = new Button("remove");
        button.addClickListener(event -> {
            recipeService.deleteRecipe(recipe);
            reloadGrid();
            Notification notification = new MiddleNotification("Recipe successfully removed from database");
            notification.open();
        });
        return button;
    }

    private Button buildCreateButton() {
        Button button = new Button("Create new recipe");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(click -> {
            dialog = buildCreatorDialog();
            dialog.open();
        });
        return button;
    }

    private Dialog buildCreatorDialog() {
        Dialog dialog = new Dialog();
        HorizontalLayout buttonBar = new HorizontalLayout();
        recipeCreator = new RecipeCreator(ingredientService);
        buttonBar.add(buildSaveButton(), buildCloseButton());
        dialog.add(recipeCreator, buttonBar);
        return dialog;
    }

    private Dialog buildCreatorDialog(Recipe recipe) {
        Dialog dialog = new Dialog();
        HorizontalLayout buttonBar = new HorizontalLayout();
        recipeCreator = new RecipeCreator(ingredientService, recipe);
        buttonBar.add(buildUpdateButton(recipe), buildCloseButton());
        dialog.add(recipeCreator, buttonBar);
        return dialog;
    }

    private Button buildSaveButton() {
        Button button = new Button("create recipe");
        button.setIcon(IronIcons.SAVE.create());
        button.addClickListener(click -> {
            try {
                Recipe recipe = recipeCreator.getCreatedRecipe();
                recipeService.addRecipe(recipe);
                dialog.close();
                reloadGrid();
                Notification notification = new MiddleNotification("Recipe successfully added to database");
                notification.open();
            } catch (NotNumberException | EntityAlreadyExistException e) {
                Notification notification = new MiddleNotification(e.getMessage());
                notification.open();
            }
        });

        return button;
    }

    private Button buildUpdateButton(Recipe existingRecipe) {
        Button button = new Button("update recipe");
        button.setIcon(IronIcons.SAVE.create());
        button.addClickListener(click -> {
            try {
                Recipe recipe = recipeCreator.getCreatedRecipe();
                recipeService.updateRecipe(recipe, existingRecipe.getId());
                dialog.close();
                reloadGrid();
                Notification notification = new MiddleNotification("Recipe successfully updated");
                notification.open();
            } catch (RecipeNotFoundException e) {
                Notification notification = new MiddleNotification(e.getMessage());
                notification.open();
            }
        });
        return button;
    }


    private Button buildCloseButton() {
        Button button = new Button("close");
        button.setIcon(IronIcons.CANCEL.create());
        button.addClickListener(click -> {
            dialog.close();
            reloadGrid();
        });
        return button;
    }

}