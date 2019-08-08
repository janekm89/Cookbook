package pl.chief.cookbook.gui.views;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.gui.components.BoldLabel;
import pl.chief.cookbook.gui.components.RecipeCreatorDialog;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.gui.layout.RecipeCreatorBar;
import pl.chief.cookbook.gui.layout.RecipeEditor;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;

import java.util.Map;

@Component
@Route("recipe-manager")
public class RecipeManager extends VerticalLayout {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private Grid<Recipe> recipeGrid;
    private RecipeCreatorBar recipeCreatorBar;
    private Map<Integer, Double> selectedIngredientAmount;

    @Autowired
    RecipeManager(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;

        AppLayout appLayout = new AppLayout();
        MenuLayout menuLayout = new MenuLayout(appLayout);
        VerticalLayout appContent = new VerticalLayout();


        VerticalLayout recipeCreatorButton = buildRecipeCreatorButton();
        Label recipeLabel = new BoldLabel("List of recipes:");
        recipeGrid = buildAllRecipeGrid();

        appContent.add(recipeLabel, recipeGrid, new Hr(), recipeCreatorButton);
        appContent.setWidth("80%");
        appLayout.setContent(appContent);

        add(appLayout);
    }

    private Grid<Recipe> buildAllRecipeGrid() {
        recipeGrid = new Grid<>(Recipe.class);
        recipeGrid.setItems(recipeService.findAllRecipes());
        recipeGrid.setColumns("name", "description", "calories", "recipeCategory");
        recipeGrid.getColumnByKey("calories").setHeader("kcal");
        recipeGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("remove");
        recipeGrid.addItemClickListener(event -> {
                    int id = event.getItem().getId();
                    Dialog dialog = new Dialog();
                    dialog.open();
                    RecipeEditor recipeEditor1 = new RecipeEditor(recipeService, ingredientService, recipeService.findRecipeById(id), dialog);
                    dialog.add(recipeEditor1);
                }
        );
        return recipeGrid;
    }

    private Button buildDeleteButton(Recipe recipe) {
        Button button = new Button("remove");
        button.addClickListener(
                buttonClickEvent -> {
                    recipeService.deleteRecipe(recipe);
                    recipeGrid.setItems(recipeService.findAllRecipes());
                    Notification notification = new Notification(
                            "Recipe sucessfully removed from database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();
                });
        return button;
    }


    private VerticalLayout buildRecipeCreatorButton() {
        VerticalLayout recipeCreatorButtonLayout = new VerticalLayout();
        Button recipeCreatorButton = new Button("Create new recipe");
        recipeCreatorButton.addClickListener(buttonClickEvent -> {
            RecipeCreatorDialog recipeCreatorDialog = new RecipeCreatorDialog(recipeService, ingredientService);
            recipeCreatorDialog.open();
            recipeCreatorDialog.addDialogCloseActionListener(event -> recipeGrid = buildAllRecipeGrid());
        });
        recipeCreatorButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        recipeCreatorButtonLayout.add(recipeCreatorButton);
        recipeCreatorButtonLayout.setHorizontalComponentAlignment(Alignment.END, recipeCreatorButton);

        return recipeCreatorButtonLayout;
    }

}
//214


/*
package pl.chief.cookbook.gui;


        import com.vaadin.flow.component.applayout.AppLayout;
        import com.vaadin.flow.component.button.Button;
        import com.vaadin.flow.component.combobox.ComboBox;
        import com.vaadin.flow.component.dialog.Dialog;
        import com.vaadin.flow.component.grid.Grid;
        import com.vaadin.flow.component.html.Hr;
        import com.vaadin.flow.component.html.Label;
        import com.vaadin.flow.component.html.Span;
        import com.vaadin.flow.component.notification.Notification;
        import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
        import com.vaadin.flow.component.orderedlayout.VerticalLayout;
        import com.vaadin.flow.component.textfield.TextField;
        import com.vaadin.flow.data.renderer.ComponentRenderer;
        import com.vaadin.flow.router.Route;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;
        import pl.chief.cookbook.exception.NotNumberException;
        import pl.chief.cookbook.gui.components.BoldLabel;
        import pl.chief.cookbook.gui.components.MiddleNotification;
        import pl.chief.cookbook.gui.components.SelectedIngredientGrid;
        import pl.chief.cookbook.gui.layout.MenuLayout;
        import pl.chief.cookbook.gui.layout.RecipeCreatorBar;
        import pl.chief.cookbook.model.Ingredient;
        import pl.chief.cookbook.model.Recipe;
        import pl.chief.cookbook.service.IngredientService;
        import pl.chief.cookbook.service.RecipeService;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Component
@Route("recipe-manager")
public class RecipeManager extends VerticalLayout {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private Grid<Recipe> recipeGrid;
    private RecipeCreatorBar recipeCreatorBar;
    private Map<Integer, Double> selectedIngredientAmount;
    private List<Ingredient> selectedIngredientList;
    private SelectedIngredientGrid selectedIngredientGrid;
    private TextField amountBox;
    private ComboBox<String> ingredientComboBox;

    @Autowired
    RecipeManager(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;

        Label addRecipeLabel = new BoldLabel("Create new recipe");
        AppLayout appLayout = new AppLayout();
        MenuLayout menuLayout = new MenuLayout(appLayout);
        recipeCreatorBar = new RecipeCreatorBar();
        ingredientComboBox = buildIngredientComboBox();
        selectedIngredientList = new ArrayList<>();
        selectedIngredientAmount = new HashMap<>();
        selectedIngredientGrid = new SelectedIngredientGrid();
        amountBox = new TextField("amount");
        Label selectedIngredientLabel = new Label("current selection:");
        Button addIngredientButton = buildAddIngredientButton();

        Label recipeLabel = new BoldLabel("List of recipes:");
        recipeGrid = buildAllRecipeGrid();
        recipeGrid.setWidth("80%");

        HorizontalLayout ingredientSelectorBar = new HorizontalLayout();
        ingredientSelectorBar.add(ingredientComboBox, amountBox, addIngredientButton);
        ingredientSelectorBar.setAlignItems(Alignment.END);

        VerticalLayout ingredientSelector = new VerticalLayout();
        ingredientSelector.add(ingredientSelectorBar, selectedIngredientLabel, selectedIngredientGrid);

        VerticalLayout addRecipeButtonLayout = buildAdRecipeButton();

        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(addRecipeLabel, recipeCreatorBar, ingredientSelector, addRecipeButtonLayout, new Hr(), recipeLabel, recipeGrid);


        appLayout.setContent(layoutContent);
        add(appLayout);

    }

    private Grid<Recipe> buildAllRecipeGrid() {
        recipeGrid = new Grid<>(Recipe.class);
        recipeGrid.setItems(recipeService.findAllRecipes());
        recipeGrid.setColumns("name", "description", "calories", "recipeCategory");
        recipeGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("remove");
        recipeGrid.setWidth("80%");
        recipeGrid.addItemClickListener(event -> {
                    int id = event.getItem().getId();
                    Dialog dialog = new Dialog();
                    dialog.open();
                    RecipeEditor recipeEditor1 = new RecipeEditor(recipeService, ingredientService, recipeService.findRecipeById(id), dialog);
                    dialog.add(recipeEditor1);
                }
        );
        return recipeGrid;
    }


    private Button buildDeleteButton(Recipe recipe) {
        Button button = new Button("remove");
        button.addClickListener(
                buttonClickEvent -> {
                    recipeService.deleteRecipe(recipe);
                    recipeGrid.setItems(recipeService.findAllRecipes());
                    Notification notification = new Notification(
                            "Recipe sucessfully removed from database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();
                });
        return button;
    }

    private Button buildAddIngredientButton() {
        Button createIngredientButton = new Button("add ingredient");
        createIngredientButton.addClickListener(buttonClickEvent -> {
            Ingredient selectedIngredient = ingredientService.findIngredientByName(ingredientComboBox.getValue());
            Double amount = Double.parseDouble(amountBox.getValue());
            selectedIngredientList.add(selectedIngredient);
            selectedIngredientAmount.put(selectedIngredient.getId(), amount);
            selectedIngredientGrid.setItems(selectedIngredientList);

            MiddleNotification notification = new MiddleNotification("Ingredient added");
            notification.open();

        });
        return createIngredientButton;
    }

    private VerticalLayout buildAdRecipeButton() {
        VerticalLayout addRecipeButtonLayout = new VerticalLayout();
        Button addRecipeButton = new Button("create recipe");
        addRecipeButton.addClickListener(
                buttonClickEvent -> {
                    try {
                        Recipe recipe = new Recipe();
                        recipe.setName(recipeCreatorBar.getNameField().getValue());
                        recipe.setDescription(recipeCreatorBar.getDescriptionField().getValue());
                        recipe.setRecipeCategory(recipeCreatorBar.getRecipeCategoryComboBox().getValue());
                        recipe.setCalories(recipeCreatorBar.getCaloriesField().getValue().intValue());
                        recipe.setIngredientsAmount(selectedIngredientAmount);
                        recipeService.addRecipe(recipe);
                    } catch (NotNumberException e) {
                        e.printStackTrace();
                    }
                    recipeGrid.setItems(recipeService.findAllRecipes());

                    Notification notification = new MiddleNotification("Recipe sucessfully added to database");
                    notification.open();
                });
        addRecipeButtonLayout.add(addRecipeButton);
        addRecipeButtonLayout.setAlignItems(Alignment.END);
        addRecipeButtonLayout.setWidth("80%");

        return addRecipeButtonLayout;
    }

    private ComboBox<String> buildIngredientComboBox() {
        ingredientComboBox = new ComboBox<>("choose ingredient to add");
        ingredientComboBox.setItems(ingredientService.findAllIngredientNames());
        ingredientComboBox.addValueChangeListener(event -> {
            String ingredientName = ingredientComboBox.getValue();
            String unitName = ingredientService.findUnitByIngredientName(ingredientName).name().toLowerCase();
            amountBox.setSuffixComponent(new Span(unitName));
        });
        return ingredientComboBox;
    }
}
//214*/
