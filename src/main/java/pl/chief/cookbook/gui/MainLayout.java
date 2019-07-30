package pl.chief.cookbook.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;
import pl.chief.cookbook.util.AppProperties;

import java.util.stream.Collectors;

@Route("index")
public class MainLayout extends HorizontalLayout {

    private static final String LOGO_PNG = AppProperties.logo;

    @Autowired
    MainLayout(RecipeService recipeService, IngredientService ingredientService) {

        VerticalLayout sidenav = new VerticalLayout();
        ListBox<String> listBox = new ListBox<>();
        listBox.setItems(ingredientService
                .findAllIngredients()
                .stream()
                .map(ingredient -> ingredient.getName())
                .collect(Collectors.toList()));
        Button button = new Button("find");
        sidenav.add(listBox, button);

        VerticalLayout content = new VerticalLayout();
        Grid<Recipe> grid = new Grid<>(Recipe.class);
        grid.setItems(recipeService.findAllRecipes());
        grid.removeColumnByKey("ingredients");
        grid.removeColumnByKey("ingredientsAmount");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "description", "calories");
        content.add(grid);

        add(sidenav, content);
    }


    /*    Image img = new Image("img/logo.png", "logo");
    // img.setHeight("100px");

    // TOPNAV
    AppLayout appLayout = new AppLayout();
        appLayout.setBranding(img);
    AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Find by ingredient", "find-by-ingredient"),
            new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Find by category", "find-by-category")
            );*/
}
