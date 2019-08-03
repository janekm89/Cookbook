package pl.chief.cookbook.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

import java.util.Arrays;
import java.util.List;


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
                new AppLayoutMenuItem(VaadinIcon.MENU.create(), "Find recipes", "index")
        );


        ComboBox<RecipeCategory> recipeCategoryComboBox = new ComboBox<>();
        recipeCategoryComboBox.setPlaceholder("Recipe category");
        List<RecipeCategory> recipeCategories = Arrays.asList(RecipeCategory.values());
        recipeCategoryComboBox.setItems(recipeCategories);
        Button findByCategoryButton = new Button("Find by category");
        findByCategoryButton.addClickListener(click -> {
            grid.setItems(recipeService.findByCategory(recipeCategoryComboBox.getValue()));

        });
        HorizontalLayout findByCategoryLayout = new HorizontalLayout();
        findByCategoryLayout.add(recipeCategoryComboBox, findByCategoryButton);

        TextField recipeNameTextField = new TextField();
        recipeNameTextField.setPlaceholder("recipe Name");
        Button findByNameButton = new Button("Find by name");
        findByNameButton.addClickListener(click -> {
            grid.setItems(recipeService.findRecipeByName("%" + recipeNameTextField.getValue() + "%"));
        });
        HorizontalLayout finByNameLayout = new HorizontalLayout();
        finByNameLayout.add(recipeNameTextField, findByNameButton);


        TextField recipeDescriptionTextField = new TextField();
        recipeDescriptionTextField.setPlaceholder("recipe Description");
        Button findByDescButton = new Button("Find by description");
        findByDescButton.addClickListener(click -> {
            grid.setItems(recipeService.findRecipeByDescription("%" + recipeDescriptionTextField.getValue() + "%"));
        });
        HorizontalLayout finByDescLayout = new HorizontalLayout();
        finByDescLayout.add(recipeDescriptionTextField, findByDescButton);

        TextField caloriesMinField = new TextField();
        caloriesMinField.setPlaceholder("Calories minimum");
        caloriesMinField.setMaxWidth("170px");
        TextField caloriesMaxField = new TextField();
        caloriesMaxField.setPlaceholder("Calories maximum");
        caloriesMaxField.setMaxWidth("170px");
        Button findByCaloriesButton = new Button("Find by calories");
        findByCaloriesButton.addClickListener(click -> {
            grid.setItems(recipeService.findRecipesWithCaloriesIn(
                    caloriesMinField.getValue(), caloriesMaxField.getValue()));
        });
        VerticalLayout caloriesLayout = new VerticalLayout();
        caloriesLayout.add(caloriesMinField, caloriesMaxField);
        HorizontalLayout findByCaloriesLayout = new HorizontalLayout();
        findByCaloriesButton.setMinWidth("150px");
        findByCaloriesLayout.add(caloriesLayout, findByCaloriesButton);
        findByCaloriesLayout.setAlignItems(Alignment.CENTER);


        ComboBox<String> ingredientComboBox = new ComboBox<>();
        ingredientComboBox.setPlaceholder("Ingredient");
        List<String> ingredientList = ingredientService.findAllIngredientNames();
        ingredientComboBox.setItems(ingredientList);
        Button findByIngredientButton = new Button("Find by Ingredient");
        findByIngredientButton.addClickListener(click -> {
            grid.setItems(recipeService.
                    findAllRecipesContainingIngredient(ingredientService.
                            findIngredientByName(ingredientComboBox.getValue())));
        });
        HorizontalLayout finByIngredientLayout = new HorizontalLayout();
        finByIngredientLayout.add(ingredientComboBox, findByIngredientButton);


       // Tree<String> tree = new Tree<>("Ingredients");
        TreeData<String> ingredientsTree = new TreeData<>();
        ingredientsTree.addRootItems(IngredientCategory.values().toString());
        for (IngredientCategory ingredientCategory : IngredientCategory.values()) {
            for (String ingredientName : ingredientService.findAllIngredientNamesByIngredientCategory(ingredientCategory)) {
                ingredientsTree.addItem(ingredientCategory.toString(), ingredientName);
            }
        }


        HorizontalLayout ingredientsTreeLayout = new HorizontalLayout();

        sidenav.add(findByCategoryLayout, finByNameLayout, finByDescLayout, findByCaloriesLayout, finByIngredientLayout);
        sidenav.setWidth("25%");

        grid.removeColumnByKey("ingredients");
        grid.removeColumnByKey("ingredientsAmount");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "description", "calories");

        table.add(grid);

        content.add(sidenav, table);

        appLayout.setContent(content);
        add(appLayout);
    }
}
