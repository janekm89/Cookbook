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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

@Route("recipe-manager")
public class RecipeManager extends VerticalLayout {

    private Grid<Recipe> recipeGrid;

    @Autowired
    RecipeService recipeService;

    RecipeManager (RecipeService recipeService) {

        Image logo = new Image( ImagePath.LOGO, "logo");
        logo.setHeight("100px");

        AppLayout appLayout = new AppLayout();
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.MENU.create(), "Find recipes", "index"));


        TextField nameField = new TextField();
        nameField.setLabel("recipe name");

        TextField descriptionField = new TextField();
        descriptionField.setLabel("description");
        descriptionField.setHeight("1500px");

        ComboBox<RecipeCategory> recipeCategoryComboBox = new ComboBox<>("recipe category");
        recipeCategoryComboBox.setItems(RecipeCategory.values());

        NumberField caloriesField = new NumberField("Calories");
        caloriesField.setValue(1d);
        caloriesField.setMin(0);
        caloriesField.setMax(500);
        caloriesField.setHasControls(true);



        Button button = new Button("Add");
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

        HorizontalLayout recipeEditor = new HorizontalLayout();
        recipeEditor.add(nameField, descriptionField, recipeCategoryComboBox, caloriesField, button);
        recipeEditor.setVerticalComponentAlignment(Alignment.END, button);


        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(recipeEditor);

        appLayout.setContent(layoutContent);
        add(appLayout);
    }
}
