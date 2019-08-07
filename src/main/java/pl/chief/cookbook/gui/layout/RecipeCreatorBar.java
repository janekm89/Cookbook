package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.chief.cookbook.gui.components.CaloriesField;
import pl.chief.cookbook.gui.components.DescriptionField;
import pl.chief.cookbook.gui.components.RecipeCategoryComboBox;
import pl.chief.cookbook.model.Recipe;


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

    Recipe getCreatedRecipe(){
        Recipe recipe = new Recipe();
        recipe.setName(nameField.getValue());
        recipe.setDescription(descriptionField.getValue());
        recipe.setRecipeCategory(recipeCategoryComboBox.getValue());
        recipe.setCalories(caloriesField.getValue().intValue());
        return recipe;
    }
}
