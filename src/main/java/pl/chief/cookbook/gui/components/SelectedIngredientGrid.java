package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.grid.Grid;
import lombok.Getter;
import lombok.Setter;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SelectedIngredientGrid extends Grid<Ingredient> {

    private Map<Integer, Double> selectedIngredientAmount;

    public SelectedIngredientGrid() {
        super(Ingredient.class);
        this.removeColumnByKey("recipes");
        this.setColumns("name", "ingredientCategory");
        this.addColumn(ingredient -> selectedIngredientAmount.get(ingredient.getId())).setHeader("Amount");
        this.addColumn(Ingredient::getUnit).setHeader("Unit");
        this.setWidth("80%");
        this.setHeightByRows(true);
    }


}
