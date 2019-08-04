package pl.chief.cookbook.gui;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import javafx.beans.binding.DoubleBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route("recipe-manager")
public class RecipeManager extends VerticalLayout {

    private Grid<Recipe> recipeGrid;

    @Autowired
    RecipeService recipeService;

    @Autowired
    IngredientService ingredientService;

    RecipeManager(RecipeService recipeService, IngredientService ingredientService) {

        Image logo = new Image(ImagePath.LOGO, "logo");
        logo.setHeight("100px");

        AppLayout appLayout = new AppLayout();
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.SEARCH.create(), "Find recipes", "index"));


        TextField nameField = new TextField();
        nameField.setLabel("recipe name");

        TextField descriptionField = new TextField();
        descriptionField.setLabel("description");
        descriptionField.setHeight("1500px");

        ComboBox<RecipeCategory> recipeCategoryComboBox = new ComboBox<>("recipe category");
        recipeCategoryComboBox.setItems(RecipeCategory.values());

        NumberField caloriesField = new NumberField("Calories");

        caloriesField.setValue(10d);
        caloriesField.setMin(0);
        caloriesField.setMax(500);
        caloriesField.setStep(10);
        caloriesField.setHasControls(true);

        HorizontalLayout recipeEditor = new HorizontalLayout();
        recipeEditor.add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);
        // recipeEditor.setVerticalComponentAlignment(Alignment.END, button);
        recipeEditor.setHeight("100px");


//  Adding ingredients

        ComboBox<String> ingredientBox = new ComboBox<>("choose ingredient to add");
        List<Ingredient> selectedIngredientList = new ArrayList<>();
        Map<Integer, Double> selectedIngredientAmount = new HashMap<>();
        Grid<Ingredient> selectedIngredientGrid = new Grid<>(Ingredient.class);


        Label selectedIngredientLabel = new Label("current selection:");

        TextField amountBox = new TextField("amount");


        ingredientBox.setItems(ingredientService.findAllIngredientNames());

        selectedIngredientGrid.removeColumnByKey("recipes");
        selectedIngredientGrid.setColumns("name", "ingredientCategory");
        selectedIngredientGrid.addColumn(ingredient -> selectedIngredientAmount.get(ingredient.getId())).setHeader("Amount");
        selectedIngredientGrid.addColumn(Ingredient::getUnit).setHeader("Unit");
        selectedIngredientGrid.setWidth("80%");
        selectedIngredientGrid.setHeightByRows(true);

        ingredientBox.addValueChangeListener(event -> {
            String ingredientName = ingredientBox.getValue();
            String unitName = ingredientService.findUnitByIngredientName(ingredientName).name().toLowerCase();
            amountBox.setSuffixComponent(new Span(unitName));
        });

        Button addIngredientButton = new Button("add ingredient");
        addIngredientButton.addClickListener(buttonClickEvent -> {
            Ingredient selectedIngredient = ingredientService.findIngredientByName(ingredientBox.getValue());
            Double amount = Double.parseDouble(amountBox.getValue());
            selectedIngredientList.add(selectedIngredient);
            selectedIngredientAmount.put(selectedIngredient.getId(), amount);
            selectedIngredientGrid.setItems(selectedIngredientList);


            Notification notification = new Notification(
                    "Ingredient added", 3000,
                    Notification.Position.TOP_START);
            notification.open();

        });

        Button addRecipe = new Button("create recipe");
        addRecipe.addClickListener(
                buttonClickEvent -> {
                    try {
                        Recipe recipe = new Recipe();
                        recipe.setName(nameField.getValue());
                        recipe.setDescription(descriptionField.getValue());
                        recipe.setRecipeCategory(recipeCategoryComboBox.getValue());
                        recipe.setCalories(caloriesField.getValue().intValue());
                        recipe.setIngredientsAmount(selectedIngredientAmount);
                        recipeService.addRecipe(recipe);
                    } catch (NotNumberException e) {
                        e.printStackTrace();
                    }
                    recipeGrid.setItems(recipeService.findAllRecipes());

                    Notification notification = new Notification(
                            "Recipe sucessfully added to database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();
                });


        Label recipeLabel = new Label("List of recipes:");
        recipeLabel.getStyle().set("font-weight", "bold");
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


        HorizontalLayout ingredientSelectorBar = new HorizontalLayout();
        ingredientSelectorBar.add(ingredientBox, amountBox, addIngredientButton);
        ingredientSelectorBar.setAlignItems(Alignment.END);


        VerticalLayout ingredientSelector = new VerticalLayout();
        ingredientSelector.add(ingredientSelectorBar, selectedIngredientLabel, selectedIngredientGrid);

        VerticalLayout addRecipeButtonLayout = new VerticalLayout();
        addRecipeButtonLayout.add(addRecipe);
        addRecipeButtonLayout.setAlignItems(Alignment.END);
        addRecipeButtonLayout.setWidth("80%");

        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(recipeEditor, new Hr(), ingredientSelector, addRecipeButtonLayout, new Hr(), recipeLabel, recipeGrid);


        appLayout.setContent(layoutContent);
        add(appLayout);


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
}
