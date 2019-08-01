package pl.chief.cookbook.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.ImagePath;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@Route("index")
public class MainLayout extends VerticalLayout {

    @Autowired
    MainLayout(RecipeService recipeService, IngredientService ingredientService) {

        Image logo = new Image(ImagePath.LOGO, "logo");
        logo.setHeight("100px");

        AppLayout appLayout = new AppLayout();
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.MENU.create(), "Find recipes", "index")
        );

        VerticalLayout sidenav = new VerticalLayout();

        ListBox<String> listBox = new ListBox<>();
        listBox.setItems(ingredientService
                .findAllIngredients()
                .stream()
                .map(ingredient -> ingredient.getName())
                .collect(Collectors.toList()));


        Button button = new Button("find");


        sidenav.add(listBox, button);
        sidenav.setWidth("10%");


        Grid<Recipe> grid = new Grid<>(Recipe.class);
        grid.setItems(recipeService.findAllRecipes());
        grid.removeColumnByKey("ingredients");
        grid.removeColumnByKey("ingredientsAmount");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "description", "calories");


        VerticalLayout table = new VerticalLayout();
        table.add(grid);

        HorizontalLayout content = new HorizontalLayout();
        content.add(sidenav, table);

        appLayout.setContent(content);
        add(appLayout);
    }
}
