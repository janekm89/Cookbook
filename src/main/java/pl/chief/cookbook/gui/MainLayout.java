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
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
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
            grid.setItems(recipeService.findRecipeByName(recipeNameTextField.getValue()));
        });
        HorizontalLayout finByNameLayout = new HorizontalLayout();
        finByNameLayout.add(recipeNameTextField, findByNameButton);


        sidenav.add(findByCategoryLayout, finByNameLayout);
        sidenav.setWidth("20%");


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
