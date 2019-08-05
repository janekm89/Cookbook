package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import pl.chief.cookbook.gui.components.CaloriesField;
import pl.chief.cookbook.gui.components.DescriptionField;
import pl.chief.cookbook.gui.components.RecipeCategoryComboBox;

@Getter
@Setter
public class RecipeCreatorBar extends HorizontalLayout {
    private TextField nameField;
    private DescriptionField descriptionField;
    private RecipeCategoryComboBox recipeCategoryComboBox;
    private CaloriesField caloriesField;

    public RecipeCreatorBar() {
        this.nameField = new TextField();
        this.nameField.setLabel("recipe name");
        this.descriptionField = new DescriptionField();
        this.recipeCategoryComboBox = new RecipeCategoryComboBox();
        this.caloriesField = new CaloriesField();
        this.setHeight("100px");

        add(nameField, descriptionField, recipeCategoryComboBox, caloriesField);
    }
}
