package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.gui.layout.RecipeView;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Route("")
public class MainLayout extends VerticalLayout {
    private VerticalLayout sidenav;
    private Grid<Recipe> grid;
    private TextField recipeNameTextField;
    private TextField recipeDescriptionTextField;
    private TextField caloriesMinField;
    private TextField caloriesMaxField;
    private MultiselectComboBox<String> multiSelectIngredient;
    private ComboBox<RecipeCategory> recipeCategoryComboBox;
    private boolean wasSearchAndIsEmpty;
    private RecipeService recipeService;
    private IngredientService ingredientService;


    @Autowired
    public MainLayout(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        sidenav = new VerticalLayout();
        AppLayout appLayout = new AppLayout();
        MenuLayout menuLayout = new MenuLayout(appLayout);
        VerticalLayout table = new VerticalLayout();
        HorizontalLayout content = new HorizontalLayout();

        setRecipesGridProperties();
        createSearchFields();
        setSideNavProperties();

        table.add(grid);
        content.add(sidenav, table);
        appLayout.setContent(content);
        add(appLayout);
    }

    private void setSideNavProperties() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button searchButton = createSearchButton();
        Button clearButton = createClearButton();
        buttonLayout.add(searchButton, clearButton);
        sidenav.add(recipeCategoryComboBox, recipeNameTextField, recipeDescriptionTextField,
                caloriesMinField, caloriesMaxField, multiSelectIngredient,
                buttonLayout);
        sidenav.setWidth("25%");
    }

    private Button createClearButton() {
        Button clearButton = new Button("Clear");
        clearButton.addClickListener(click -> {
            recipeCategoryComboBox.clear();
            recipeNameTextField.clear();
            recipeDescriptionTextField.clear();
            caloriesMinField.clear();
            caloriesMaxField.clear();
            multiSelectIngredient.clear();
        });
        return clearButton;
    }


    private void retainCollectionsIfNotEmpty(List<Recipe> containingAll, List<Recipe> newElements) {
        if (!containingAll.isEmpty() && !newElements.isEmpty()) {
            containingAll.retainAll(newElements);
        } else if (containingAll.isEmpty() && !newElements.isEmpty() && !wasSearchAndIsEmpty) {
            containingAll.addAll(newElements);
        } else if (!containingAll.isEmpty()) {
            containingAll.clear();
            wasSearchAndIsEmpty = true;
        }
    }

    private void addRecipesToGrid(List<Recipe> recipes) throws RecipeNotFoundException {
        if (recipes.isEmpty() || recipes.get(0).getName().isEmpty())
            throw new RecipeNotFoundException();
        if (!wasSearchAndIsEmpty)
            grid.setItems(recipes);
    }

    private void searchRecipesByCategoryIfNeeded(List<Recipe> allRecipes) {
        if (recipeCategoryComboBox.getValue() != null) {
            retainCollectionsIfNotEmpty(allRecipes, recipeService.findByCategory(recipeCategoryComboBox.getValue()));
        }
    }

    private void searchByNameFieldIfNeeded(List<Recipe> allRecipes) {
        if (!recipeNameTextField.getValue().isEmpty()) {
            retainCollectionsIfNotEmpty(allRecipes, recipeService.findRecipeByName("%" + recipeNameTextField.getValue() + "%"));
        }
    }

    private void searchByDescriptionFieldIfNeeded(List<Recipe> allRecipes) {
        if (!recipeDescriptionTextField.getValue().isEmpty()) {
            retainCollectionsIfNotEmpty(allRecipes, recipeService.findRecipeByDescription("%" + recipeDescriptionTextField.getValue() + "%"));
        }
    }

    private void searchByDoubleTextFieldIfNeeded(List<Recipe> allRecipes) throws NotNumberException {
        if (!caloriesMinField.getValue().isEmpty() || !caloriesMaxField.getValue().isEmpty()) {
            retainCollectionsIfNotEmpty(allRecipes, recipeService.findRecipesWithCaloriesIn(caloriesMinField.getValue(), caloriesMaxField.getValue()));
        }
    }

    private void searchByMultiSelectIfNeeded(List<Recipe> allRecipes) throws RecipeNotFoundException {
        if (!multiSelectIngredient.getSelectedItems().isEmpty()) {
            List<Ingredient> ingredients = multiSelectIngredient.getSelectedItems().stream()
                    .map(ingredientService::findIngredientByName).collect(Collectors.toList());
            retainCollectionsIfNotEmpty(allRecipes, recipeService
                    .findRecipesWithIds(recipeService.findRecipesIdWithIngredients(ingredients)));
        }
    }

    private void setRecipesGridProperties() {
        grid = new Grid<>(Recipe.class);
        grid.removeColumnByKey("ingredients");
        grid.removeColumnByKey("ingredientsAmount");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "description", "calories", "recipeCategory");
        addClickOnRecipeListener();
    }

    private void addClickOnRecipeListener() {
        grid.addItemClickListener(click -> {
            RecipeView recipeView = new RecipeView(recipeService, ingredientService, click.getItem().getId());
            Dialog dialog = new Dialog();
            dialog.setWidth("1000px");
            dialog.setHeight("500px");
            dialog.open();
            Button cancelButton = new Button("Close", event -> {
                dialog.close();
            });
            dialog.add(recipeView, cancelButton);
        });
    }

    private Button createSearchButton() {
        Button button = new Button("Search");
        button.addClickListener(click -> {
            List<Recipe> allRecipes = new ArrayList<>();
            wasSearchAndIsEmpty = false;
            MiddleNotification notification = new MiddleNotification();
            try {
                searchRecipesByCategoryIfNeeded(allRecipes);
                searchByNameFieldIfNeeded(allRecipes);
                searchByDescriptionFieldIfNeeded(allRecipes);
                searchByDoubleTextFieldIfNeeded(allRecipes);
                searchByMultiSelectIfNeeded(allRecipes);
                addRecipesToGrid(allRecipes);
            } catch (RecipeNotFoundException | NotNumberException e) {
                grid.setItems(new ArrayList<>());
                notification.setText(e.getMessage());
                notification.open();
            }
        });
        button.addClickShortcut(Key.ENTER);
        return button;
    }


    private void createSearchFields() {
        recipeCategoryComboBox = new ComboBox<>();
        recipeCategoryComboBox.setPlaceholder("Recipe category");
        recipeCategoryComboBox.setItems(Arrays.asList(RecipeCategory.values()));

        recipeNameTextField = new TextField();
        recipeNameTextField.setPlaceholder("recipe Name");

        recipeDescriptionTextField = new TextField();
        recipeDescriptionTextField.setPlaceholder("recipe Description");

        caloriesMinField = new TextField();
        caloriesMinField.setPlaceholder("Calories minimum");
        caloriesMaxField = new TextField();
        caloriesMaxField.setPlaceholder("Calories maximum");

        multiSelectIngredient = new MultiselectComboBox<>();
        multiSelectIngredient.setItems(ingredientService.findAllIngredientNames());
        multiSelectIngredient.setPlaceholder("Ingredients");
    }
}
