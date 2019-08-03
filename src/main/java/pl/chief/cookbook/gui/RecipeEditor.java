package pl.chief.cookbook.gui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.RecipeService;

import java.awt.*;

public class RecipeEditor extends HorizontalLayout {


    @Autowired
    public RecipeEditor(RecipeService recipeService, Recipe recipe) {
        TextField nameField = new TextField();
        nameField.setValue(recipe.getName());
        nameField.setLabel("recipe name");

        TextField descriptionField = new TextField();
        descriptionField.setValue(recipe.getDescription());
        descriptionField.setLabel("description");

        ComboBox<RecipeCategory> recipeCategoryComboBox = new ComboBox<>("recipe category");
        recipeCategoryComboBox.setItems(RecipeCategory.values());
        recipeCategoryComboBox.setValue(recipe.getRecipeCategory());

        NumberField caloriesField = new NumberField("Calories");

        caloriesField.setValue(10d);
        caloriesField.setMin(0);
        caloriesField.setMax(500);
        caloriesField.setStep(10);
        caloriesField.setHasControls(true);
        caloriesField.setValue((double) recipe.getCalories());


        HorizontalLayout recipeEditor = new HorizontalLayout();
        recipeEditor.add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);
        recipeEditor.setHeight("100px");


        add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);

    }
}
