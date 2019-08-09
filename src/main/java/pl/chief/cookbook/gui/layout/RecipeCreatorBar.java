package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import pl.chief.cookbook.gui.components.CaloriesField;
import pl.chief.cookbook.gui.components.DescriptionField;
import pl.chief.cookbook.gui.components.RecipeCategoryComboBox;
import pl.chief.cookbook.model.Recipe;

@Setter
@Getter
class RecipeCreatorBar extends HorizontalLayout {
    private TextField nameField;
    private DescriptionField descriptionField;
    private RecipeCategoryComboBox recipeCategoryComboBox;
    private CaloriesField caloriesField;

    RecipeCreatorBar() {
        this.nameField = new TextField();
        this.nameField.setLabel("recipe name");
        this.descriptionField = new DescriptionField();
        this.recipeCategoryComboBox = new RecipeCategoryComboBox();
        this.caloriesField = new CaloriesField();
        this.setHeight("100px");

        add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);
    }

    RecipeCreatorBar(Recipe recipe) {
        this();
        this.nameField.setValue(recipe.getName());
        this.descriptionField.setValue(recipe.getDescription());
        this.recipeCategoryComboBox.setValue(recipe.getRecipeCategory());
        this.caloriesField.setValue((double) recipe.getCalories());
    }

    Recipe getCreatedRecipe(){
        Recipe recipe = new Recipe();
        recipe.setName(nameField.getValue());
        recipe.setDescription(descriptionField.getValue());
        recipe.setRecipeCategory(recipeCategoryComboBox.getValue());
        recipe.setCalories(caloriesField.getValue().intValue());
        return recipe;
    }
}
