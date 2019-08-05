package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.gui.components.SelectedIngredientGrid;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.service.IngredientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class IngredientSelector extends VerticalLayout {
    private final IngredientService ingredientService;
    private SelectedIngredientGrid selectedIngredientGrid;
    private ComboBox<String> ingredientComboBox;
    private Map<Integer, Double> selectedIngredientAmount;
    private List<Ingredient> selectedIngredientList;
    private TextField amountBox;

    @Autowired
    public IngredientSelector(IngredientService ingredientService) {
        this.ingredientService = ingredientService;

        HorizontalLayout upperBar = buildUpperBar();
        Label selectedIngredientLabel = new Label("current selection:");
        selectedIngredientGrid = new SelectedIngredientGrid();

        add(upperBar, selectedIngredientLabel, selectedIngredientGrid);
    }

    private HorizontalLayout buildUpperBar(){
        HorizontalLayout upperBar = new HorizontalLayout();
        ingredientComboBox = buildIngredientComboBox();
        amountBox = new TextField("amount");
        Button addIngredientButton = buildAddIngredientButton();
        upperBar.add(ingredientComboBox, amountBox, addIngredientButton);
        upperBar.setAlignItems(Alignment.END);
        return upperBar;
    }

    private ComboBox<String> buildIngredientComboBox() {
        ingredientComboBox = new ComboBox<>("choose ingredient to add.");
        ingredientComboBox.setItems(ingredientService.findAllIngredientNames());
        ingredientComboBox.addValueChangeListener(event -> amountBox.setSuffixComponent(new Span(findUnitName())));
        return ingredientComboBox;
    }

    private String findUnitName(){
        String ingredientName = ingredientComboBox.getValue();
        return ingredientService.findUnitByIngredientName(ingredientName).name().toLowerCase();
    }

    private Button buildAddIngredientButton() {
        Button createIngredientButton = new Button("add ingredient.");
        selectedIngredientList = new ArrayList<>();
        selectedIngredientAmount = new HashMap<>();
        createIngredientButton.addClickListener(buttonClickEvent -> {
            Ingredient selectedIngredient = ingredientService.findIngredientByName(ingredientComboBox.getValue());
            Double amount = Double.parseDouble(amountBox.getValue());
            selectedIngredientList.add(selectedIngredient);
            selectedIngredientAmount.put(selectedIngredient.getId(), amount);
            selectedIngredientGrid.setSelectedIngredientAmount(selectedIngredientAmount);
            selectedIngredientGrid.setItems(selectedIngredientList);
            MiddleNotification notification = new MiddleNotification("Ingredient added");
            notification.open();
        });
        return createIngredientButton;
    }
}
