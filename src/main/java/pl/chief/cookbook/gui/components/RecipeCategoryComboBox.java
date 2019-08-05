package pl.chief.cookbook.gui.components;


import com.vaadin.flow.component.combobox.ComboBox;
import pl.chief.cookbook.features.RecipeCategory;

public class RecipeCategoryComboBox extends ComboBox<RecipeCategory> {
    public RecipeCategoryComboBox() {
        this.setLabel("recipe category");
        this.setItems(RecipeCategory.values());
    }
}
