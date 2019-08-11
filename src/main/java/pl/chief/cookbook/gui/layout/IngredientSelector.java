package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.gui.components.SelectedIngredientGrid;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.service.IngredientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class IngredientSelector extends VerticalLayout {
    private final IngredientService ingredientService;
    private SelectedIngredientGrid selectedIngredientGrid;
    private ComboBox<String> ingredientComboBox;
    private Map<Integer, Double> selectedIngredientAmount;
    private List<Ingredient> selectedIngredientList;
    private NumberField amountBox;

    @Autowired
    public IngredientSelector(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
        selectedIngredientList = new ArrayList<>();
        selectedIngredientAmount = new HashMap<>();

        HorizontalLayout upperBar = buildUpperBar();
        Label selectedIngredientLabel = new Label("current selection:");
        selectedIngredientGrid = new SelectedIngredientGrid();
        selectedIngredientGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("Remove Ingredient");
        reloadGrid();
        add(upperBar, selectedIngredientLabel, selectedIngredientGrid);
    }


    @Autowired
    public IngredientSelector(IngredientService ingredientService, Recipe recipe) {
        this.ingredientService = ingredientService;
        selectedIngredientList = ingredientService.findIngredientsByRecipe(recipe);
        selectedIngredientAmount = findIngredientAmountForRecipe(recipe);

        HorizontalLayout upperBar = buildUpperBar();
        Label selectedIngredientLabel = new Label("current selection:");
        selectedIngredientGrid = new SelectedIngredientGrid();
        selectedIngredientGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("Remove Ingredient");
        reloadGrid();
        selectedIngredientGrid.setSelectedIngredientAmount(selectedIngredientAmount);

        add(upperBar, selectedIngredientLabel, selectedIngredientGrid);
    }

    public Map<Integer, Double> getSelectedIngredientAmount() {
        return selectedIngredientAmount;
    }

    private Button buildDeleteButton(Ingredient ingredient) {
        Button button = new Button("Remove");
        button.addClickListener(
                buttonClickEvent -> {
                    selectedIngredientList.remove(ingredient);
                    selectedIngredientAmount.remove(ingredient.getId());
                    reloadGrid();
                });
        return button;
    }

    private void reloadGrid(){
        selectedIngredientGrid.setItems(selectedIngredientList);
    }


    private HorizontalLayout buildUpperBar() {
        HorizontalLayout upperBar = new HorizontalLayout();
        ingredientComboBox = buildIngredientComboBox();
        amountBox = new NumberField("amount");
        Button addIngredientButton = buildAddIngredientButton();
        upperBar.add(ingredientComboBox, amountBox, addIngredientButton);
        upperBar.setAlignItems(Alignment.END);
        return upperBar;
    }

    private Button buildAddIngredientButton() {
        Button createIngredientButton = new Button("add ingredient.");
        createIngredientButton.addClickListener(click -> {
            try {
                Ingredient selectedIngredient = ingredientService.findIngredientByName(ingredientComboBox.getValue());
                Double amount = amountBox.getValue();
                addIngredientToSelectedIngredientGrid(selectedIngredient, amount);
                System.out.println("3 " + this.getSelectedIngredientAmount().toString());
                MiddleNotification notification = new MiddleNotification("Ingredient successfully added", 1000);
                notification.open();
            } catch (EntityAlreadyExistException e) {
                MiddleNotification notification = new MiddleNotification("Ingredient already added");
                notification.open();
            }

        });
        return createIngredientButton;
    }

    private void addIngredientToSelectedIngredientGrid(Ingredient selectedIngredient, Double amount) throws EntityAlreadyExistException {
        checkIfIngredientIsAlreadySelected(selectedIngredient);
        selectedIngredientAmount.put(selectedIngredient.getId(), amount);


        selectedIngredientList = selectedIngredientGrid
                .getDataProvider()
                .fetch(new Query<>())
                .collect(Collectors.toList());
        selectedIngredientList.add(selectedIngredient);
        reloadGrid();
        selectedIngredientGrid.setSelectedIngredientAmount(selectedIngredientAmount);

    }

    private ComboBox<String> buildIngredientComboBox() {
        ingredientComboBox = new ComboBox<>("choose ingredient to add.");
        ingredientComboBox.setItems(ingredientService.findAllIngredientNames());
        ingredientComboBox.addValueChangeListener(event -> amountBox.setSuffixComponent(new Span(findUnitName())));
        return ingredientComboBox;
    }

    private String findUnitName() {
        String ingredientName = ingredientComboBox.getValue();
        return ingredientService.findUnitByIngredientName(ingredientName).name().toLowerCase();
    }

    private void checkIfIngredientIsAlreadySelected(Ingredient ingredient) throws EntityAlreadyExistException {
        if (selectedIngredientList != null && selectedIngredientList.contains(ingredient)) {
            throw new EntityAlreadyExistException(ingredient.getName());
        }

    }

    public Map<Integer, Double> findIngredientAmountForRecipe(Recipe recipe) {
        Map<Integer, Double> selectedIngredientAmountToReturn = new HashMap<>();
        for (Ingredient ingredient : ingredientService.findIngredientsByRecipe(recipe)) {
            int ingredientId = ingredient.getId();
            Double ingredientAmount = ingredientService.findIngredientAmountByIngredientIdAndRecipeId(ingredientId, recipe.getId());
            selectedIngredientAmountToReturn.put(ingredientId, ingredientAmount);
        }
        return selectedIngredientAmountToReturn;
    }

}
