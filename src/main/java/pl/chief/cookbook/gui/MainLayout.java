package pl.chief.cookbook.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;
import pl.chief.cookbook.exception.NotNumberException;
import pl.chief.cookbook.exception.RecipeNotFoundException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Route("index")
public class MainLayout extends VerticalLayout {

    @Autowired
    MainLayout(RecipeService recipeService, IngredientService ingredientService) {
        Image logo = new Image(ImagePath.LOGO, "logo");
        AppLayout appLayout = new AppLayout();
        VerticalLayout sidenav = new VerticalLayout();
        Grid<Recipe> grid = new Grid<>(Recipe.class);
        VerticalLayout table = new VerticalLayout();
        HorizontalLayout content = new HorizontalLayout();

        logo.setHeight("100px");
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.SEARCH.create(), "Find recipes", "index")
        );


        ComboBox<RecipeCategory> recipeCategoryComboBox = new ComboBox<>();
        recipeCategoryComboBox.setPlaceholder("Recipe category");
        List<RecipeCategory> recipeCategories = Arrays.asList(RecipeCategory.values());
        recipeCategoryComboBox.setItems(recipeCategories);


        TextField recipeNameTextField = new TextField();
        recipeNameTextField.setPlaceholder("recipe Name");

        TextField recipeDescriptionTextField = new TextField();
        recipeDescriptionTextField.setPlaceholder("recipe Description");

        TextField caloriesMinField = new TextField();
        caloriesMinField.setPlaceholder("Calories minimum");
        caloriesMinField.setMaxWidth("170px");
        TextField caloriesMaxField = new TextField();
        caloriesMaxField.setPlaceholder("Calories maximum");
        caloriesMaxField.setMaxWidth("170px");

        List<String> ingredientList = ingredientService.findAllIngredientNames();
        MultiselectComboBox<String> multiSelectIngredient = new MultiselectComboBox<>();
        multiSelectIngredient.setItems(ingredientList);
        multiSelectIngredient.setPlaceholder("Ingredients");
        Button searchGeneralButton = new Button("Search");

        searchGeneralButton.addClickListener(click -> {
            List<Recipe> allRecipes = new ArrayList<>();
            List<Recipe> categoryRecipes;
            List<Recipe> nameRecipes;
            List<Recipe> descriptionRecipes;
            List<Recipe> caloriesRecipes;
            List<Recipe> ingredientRecipes;
            Notification notification = new Notification();
            notification.setDuration(5000);
            notification.setPosition(Notification.Position.MIDDLE);

            try {
                if (recipeCategoryComboBox.getValue() != null) {
                    categoryRecipes = recipeService.findByCategory(recipeCategoryComboBox.getValue());
                    retainCollectionsIfNotEmpty(allRecipes, categoryRecipes);
                }
                if (!recipeNameTextField.getValue().isEmpty()) {
                    nameRecipes = recipeService.findRecipeByName("%" + recipeNameTextField.getValue() + "%");
                    retainCollectionsIfNotEmpty(allRecipes, nameRecipes);
                }
                if (!recipeDescriptionTextField.getValue().isEmpty()) {
                    descriptionRecipes = recipeService.findRecipeByName("%" + recipeDescriptionTextField.getValue() + "%");
                    retainCollectionsIfNotEmpty(allRecipes, descriptionRecipes);
                }
                if (!caloriesMinField.getValue().isEmpty() || !caloriesMaxField.getValue().isEmpty()) {
                    caloriesRecipes = recipeService.findRecipesWithCaloriesIn(caloriesMinField.getValue(), caloriesMaxField.getValue());
                    retainCollectionsIfNotEmpty(allRecipes, caloriesRecipes);
                }
                if (!multiSelectIngredient.getSelectedItems().isEmpty()) {
                    List<Ingredient> ingredients = new ArrayList<>();
                    for (String ingrName : multiSelectIngredient.getSelectedItems()) {
                        ingredients.add(ingredientService.findIngredientByName(ingrName));
                    }
                    ingredientRecipes = recipeService
                            .findRecipesWithIds(recipeService.findRecipesIdWithIngredients(ingredients));

                    retainCollectionsIfNotEmpty(allRecipes, ingredientRecipes);
                }
                addRecipesToGrid(grid, allRecipes);

            } catch (RecipeNotFoundException | NotNumberException e) {
                grid.setItems(new ArrayList<>());
                notification.setText(e.getMessage());
                notification.open();
            }


        });

        sidenav.add(recipeCategoryComboBox, recipeNameTextField, recipeDescriptionTextField,
                caloriesMinField, caloriesMaxField, multiSelectIngredient,
                searchGeneralButton);
        sidenav.setWidth("25%");

        grid.removeColumnByKey("ingredients");
        grid.removeColumnByKey("ingredientsAmount");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "description", "calories", "recipeCategory");

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
        table.add(grid);

        content.add(sidenav, table);

        appLayout.setContent(content);
        add(appLayout);
    }

    private void retainCollectionsIfNotEmpty(List<Recipe> containingAll, List<Recipe> newElements) {
        if (!containingAll.isEmpty()) {
            containingAll.retainAll(newElements);
        } else if (!newElements.isEmpty()) {
            containingAll.addAll(newElements);
        } else
            containingAll.add(new Recipe());
    }

    private void addRecipesToGrid(Grid<Recipe> grid, List<Recipe> recipes) throws RecipeNotFoundException {
        if (recipes.isEmpty())
            throw new RecipeNotFoundException();
        grid.setItems(recipes);
    }
}
