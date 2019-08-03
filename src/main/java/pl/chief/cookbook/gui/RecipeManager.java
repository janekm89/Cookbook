package pl.chief.cookbook.gui;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

import java.util.ArrayList;
import java.util.List;


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

        ComboBox<String> ingredientBox = new ComboBox<>("Choose ingredient to add");
        List<Ingredient> selectedIngredientList = new ArrayList<>();
        Grid<Ingredient> selectedIngredientGrid = new Grid<>(Ingredient.class);


        ingredientBox.setItems(ingredientService.findAllIngredientNames());

        selectedIngredientGrid.removeColumnByKey("recipes");
        selectedIngredientGrid.setColumns("name", "unit", "ingredientCategory");
        selectedIngredientGrid.setWidth("50%");
      //  selectedIngredientGrid.setHeightFull();

     //   selectedIngredientList.add(ingredientService.findIngredientById(2));

        Button addIngredientButton = new Button("add ingredient");
        addIngredientButton.addClickListener(buttonClickEvent -> {
            selectedIngredientList.add(ingredientService.findIngredientByName(ingredientBox.getValue()));
            selectedIngredientGrid.setItems(selectedIngredientList);
            Notification notification = new Notification(
                    "Ingredient added", 3000,
                    Notification.Position.TOP_START);
            notification.open();

        });

        Label selectedIngredientLabel = new Label("Current selection:");



   /*
        Button addIngredientButton = new Button("add ingredient");

        */


        Button addRecipe = new Button("create recipe");


        VerticalLayout ingredientSelector = new VerticalLayout();
        ingredientSelector.add(ingredientBox, addIngredientButton, selectedIngredientLabel, selectedIngredientGrid);

        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(recipeEditor, new Hr(), ingredientSelector, new Hr(), addRecipe);


        appLayout.setContent(layoutContent);
        add(appLayout);
        // add( layoutContent);
    }
}



/*
        button.addClickListener(
                buttonClickEvent -> {
                    Recipe recipe = new Recipe();

                    recipe.setName(nameField.getValue());
                    recipe.setDescription(descriptionField.getValue());
                    recipe.setRecipeCategory(recipeCategoryComboBox.getValue());
                    recipe.setCalories(caloriesField.  .getValue());
                    ingredientService.addIngredient(ingredient);

                    ingredientGrid.setItems(ingredientService.findAllIngredients());


                    Notification notification = new Notification(
                            "Ingredient sucessfully added to database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();

                });*/
