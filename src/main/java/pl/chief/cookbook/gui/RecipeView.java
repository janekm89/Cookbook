package pl.chief.cookbook.gui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.service.RecipeService;


@Route("recipe")
public class RecipeView extends VerticalLayout {




    @Autowired
    RecipeView(RecipeService recipeService, IngredientService ingredientService) {
        Recipe recipe = recipeService.findRecipeById("1");
        Label nameLabel = new Label(recipe.getName());
        nameLabel.getStyle().set("font-weight", "bold");
        Label descriptionLabel = new Label(recipe.getDescription());
        Grid recipeIngredientsTable = new Grid(Ingredient.class);

        recipeIngredientsTable.setItems(ingredientService.findIngredientsByRecipe(recipe));
        recipeIngredientsTable.removeColumnByKey("recipes");
        recipeIngredientsTable.removeColumnByKey("ingredientCategory");
        recipeIngredientsTable.removeColumnByKey("id");
        recipeIngredientsTable.setColumns("name", "unit");

        recipeIngredientsTable.addColumn(o -> ingredientService
                        .findIngredientAmountByIngredientIdAndRecipeId(1, recipe.getId()))
                        .setHeader("Amount");
        recipeIngredientsTable.setHeightByRows(true);

        add(nameLabel, descriptionLabel, recipeIngredientsTable);

    }


/*

   private Grid.Column IngredientAmountColumn(int ingrId, int recipeId){
       Grid.Column column = new Grid.Column()


    }
*/

}
