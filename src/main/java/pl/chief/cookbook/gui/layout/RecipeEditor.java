package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeEditor extends VerticalLayout {
    private TextField nameField;
    private TextField descriptionField;
    private ComboBox<RecipeCategory> recipeCategoryComboBox;
    private NumberField caloriesField;
    private Map<Integer, Double> selectedIngredientAmount;


    @Autowired
    public RecipeEditor(RecipeService recipeService, IngredientService ingredientService, Recipe recipe, Dialog dialog) {


        nameField = new TextField();
        nameField.setValue(recipe.getName());
        nameField.setLabel("recipe name");

        descriptionField = new TextField();
        descriptionField.setValue(recipe.getDescription());
        descriptionField.setLabel("description");

        recipeCategoryComboBox = new ComboBox<>("recipe category");
        recipeCategoryComboBox.setItems(RecipeCategory.values());
        recipeCategoryComboBox.setValue(recipe.getRecipeCategory());

        caloriesField = new NumberField("kcal");

        caloriesField.setValue(10d);
        caloriesField.setMin(0);
        caloriesField.setMax(500);
        caloriesField.setStep(10);
        caloriesField.setHasControls(true);
        caloriesField.setValue((double) recipe.getCalories());

        ComboBox<String> ingredientBox = new ComboBox<>("choose ingredient to add");

        selectedIngredientAmount = new HashMap<>();



        for (Ingredient ingredient : ingredientService.findIngredientsByRecipe(recipe)) {
            Double ingredientAmount = ingredientService.findIngredientAmountByIngredientIdAndRecipeId(ingredient.getId(), recipe.getId());
            selectedIngredientAmount.put(ingredient.getId(), ingredientAmount);
        }


        Label selectedIngredientLabel = new Label("current selection:");

        TextField amountBox = new TextField("amount");

        ingredientBox.setItems(ingredientService.findAllIngredientNames());

        Grid<Ingredient> selectedIngredientGrid = new Grid<>(Ingredient.class);
        selectedIngredientGrid.setItems(ingredientService.findIngredientsByRecipe(recipe));
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
            selectedIngredientAmount.put(selectedIngredient.getId(), amount);
            List<Ingredient> selectedIngredientList1 = selectedIngredientGrid
                    .getDataProvider()
                    .fetch(new Query<>())
                    .collect(Collectors.toList());

            selectedIngredientList1.add(selectedIngredient);
            selectedIngredientGrid.setItems(selectedIngredientList1);


            Notification notification = new Notification(
                    "Ingredient added", 3000,
                    Notification.Position.TOP_START);
            notification.open();

        });


        HorizontalLayout recipeEditor = new HorizontalLayout();
        recipeEditor.add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);
        recipeEditor.setHeight("100px");

        HorizontalLayout ingredientSelectorBar = new HorizontalLayout();
        ingredientSelectorBar.add(ingredientBox, amountBox, addIngredientButton);
        ingredientSelectorBar.setAlignItems(Alignment.END);

        VerticalLayout ingredientSelector = new VerticalLayout();
        ingredientSelector.add(ingredientSelectorBar, selectedIngredientLabel, selectedIngredientGrid);

        Button editButton = new Button("Save changes");

        editButton.addClickListener(event -> {

            Label content = new Label("Are you sure?");
            Button buttonYes = new Button("Yes");
            Button buttonNo = new Button("No");
            Notification confirmationNotification = new Notification(content, buttonYes, buttonNo);
            confirmationNotification.open();
            buttonYes.addClickListener(yesEvent -> {
                try {
                    recipe.setName(nameField.getValue());
                    recipe.setDescription(descriptionField.getValue());
                    recipe.setRecipeCategory(recipeCategoryComboBox.getValue());
                    recipe.setCalories(caloriesField.getValue().intValue());
                    recipe.setIngredientsAmount(selectedIngredientAmount);
                    recipeService.updateRecipe(recipe, recipe.getId());
                } catch (RecipeNotFoundException e) {
                    Notification notification = new Notification(
                            "Recipe not found", 3000,
                            Notification.Position.MIDDLE);
                    notification.open();
                }
                confirmationNotification.close();
                dialog.close();
            });
            buttonNo.addClickListener(noEvent -> confirmationNotification.close());
        });


        Button closeButton = new Button("Close without saving");
        closeButton.addClickListener(event -> dialog.close());

        HorizontalLayout upper = new HorizontalLayout();
        upper.add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);


        add(upper, ingredientSelector, editButton, closeButton);


    }


}
