package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.builder.IngredientBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.gui.components.MiddleNotification;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.gui.security.UserAccess;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.security.SecurityUtils;
import pl.chief.cookbook.service.IngredientService;


@Route("ingredient-manager")
public class IngredientManagerView extends VerticalLayout {

    private Grid<Ingredient> ingredientGrid;
    private TextField nameField;
    private ComboBox<MeasurementUnit> unitComboBox;
    private ComboBox<IngredientCategory> ingredientCategoryComboBox;
    private HorizontalLayout ingredientEditor;
    private IngredientService ingredientService;
    private int loggedUserId;

    @Autowired
    IngredientManagerView(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
        this.loggedUserId = UserAccess.loggedUserId();
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);

        setFields();
        this.ingredientGrid = createIngredientGrid();
        setIngredientEditorLayout();

        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(ingredientEditor, ingredientGrid);
        layoutContent.setWidthFull();
        appLayout.setContent(layoutContent);
        add(appLayout);
    }

    private void setIngredientEditorLayout() {
        ingredientEditor = new HorizontalLayout();
        Button addButton = buildAddButton();
        ingredientEditor.add(nameField, unitComboBox, ingredientCategoryComboBox, addButton);
        ingredientEditor.setVerticalComponentAlignment(Alignment.END, addButton);
    }

    private Button buildDeleteButton(Ingredient ingredient) {
        Button button = new Button("Remove");
        button.addClickListener(
                buttonClickEvent -> {
                    if (SecurityUtils.isEditingAllowed(ingredient)) {
                        deleteIngredient(ingredient);
                        MiddleNotification notification = new MiddleNotification("Ingredient successfully removed from database");
                        notification.open();
                    } else {
                        new MiddleNotification().showNotAllowed();
                    }
                });
        return button;
    }


    private void deleteIngredient(Ingredient ingredient) {
        ingredientService.deleteIngredient(ingredient);
        ingredientGrid.setItems(ingredientService.findAllIngredients());
    }

    private Button buildEditButton(Ingredient ingredient) {
        Button button = new Button("Edit");
        button.addClickListener(
                buttonClickEvent -> {
                    if (SecurityUtils.isEditingAllowed(ingredient)) {
                        Dialog dialog = new Dialog();
                        dialog.open();
                        TextField nameField = new TextField();
                        nameField.setLabel("ingredient name");
                        nameField.setValue(ingredient.getName());

                        ComboBox<MeasurementUnit> unitComboBox = new ComboBox<>("measurement unit");
                        unitComboBox.setItems(MeasurementUnit.values());
                        unitComboBox.setValue(ingredient.getUnit());

                        ComboBox<IngredientCategory> ingredientCategoryComboBox = new ComboBox<>("ingredient category");
                        ingredientCategoryComboBox.setItems(IngredientCategory.values());
                        ingredientCategoryComboBox.setValue(ingredient.getIngredientCategory());

                        Button confirmButton = buildEditConfirmationButton(ingredient, nameField, unitComboBox, ingredientCategoryComboBox, dialog);
                        Button cancelButton = buildCancelButton(dialog);
                        dialog.add(nameField, unitComboBox, ingredientCategoryComboBox, confirmButton, cancelButton);
                    } else {
                        new MiddleNotification().showNotAllowed();
                    }
                });
        return button;
    }

    private Button buildCancelButton(Dialog dialog) {
        return new Button("Cancel", event -> {
            MiddleNotification notification = new MiddleNotification("Ingredient without changes");
            notification.open();
            dialog.close();
        });
    }

    private Button buildEditConfirmationButton(Ingredient ingredient, TextField nameField, ComboBox<MeasurementUnit> unitComboBox, ComboBox<IngredientCategory> ingredientCategoryComboBox, Dialog dialog) {
        return new Button("Edit ingredient", event -> {

            ingredient.setName(nameField.getValue());
            ingredient.setUnit(unitComboBox.getValue());
            ingredient.setIngredientCategory(ingredientCategoryComboBox.getValue());
            //ingredient.setUser_id(UserAccess.loggedUserId());
            editIngredient(ingredient);
            MiddleNotification notification = new MiddleNotification("Ingredient edited in database");
            notification.open();
            dialog.close();
        });
    }

    private Button buildAddButton() {
        Button addButton = new Button("Add");

        addButton.addClickListener(
                event -> {
                    try {
                        Ingredient ingredient = new IngredientBuilder()
                                .withName(nameField.getValue())
                                .withUnit(unitComboBox.getValue())
                                .withCategory(ingredientCategoryComboBox.getValue())
                                .withUserId(UserAccess.loggedUserId())
                                .createIngredient();
                        ingredientService.addIngredient(ingredient);
                        ingredientGrid.setItems(ingredientService.findAllIngredients());
                        MiddleNotification notification = new MiddleNotification("Ingredient successfully added to database");
                        notification.open();
                    } catch (EntityAlreadyExistException e) {
                        MiddleNotification notification = new MiddleNotification(e.getMessage());
                        notification.open();
                    }
                });

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return addButton;
    }

    private void editIngredient(Ingredient ingredient) {
        ingredientService.updateIngredient(ingredient, ingredient.getId());
        ingredientGrid.setItems(ingredientService.findAllIngredients());
    }

    private void setFields() {
        nameField = new TextField();
        nameField.setLabel("ingredient name");
        unitComboBox = new ComboBox<>("measurement unit");
        unitComboBox.setItems(MeasurementUnit.values());
        ingredientCategoryComboBox = new ComboBox<>("ingredient category");
        ingredientCategoryComboBox.setItems(IngredientCategory.values());
    }

    public Grid<Ingredient> createIngredientGrid() {
        ingredientGrid = new Grid<>(Ingredient.class);
        ingredientGrid.setItems(ingredientService.findAllIngredients());
        ingredientGrid.removeColumnByKey("recipes");
        ingredientGrid.setColumns("name", "unit", "ingredientCategory");
        ingredientGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("Remove");
        ingredientGrid.addColumn(new ComponentRenderer<>(this::buildEditButton)).setHeader("Edit");
        ingredientGrid.getColumns()
                .forEach(column -> column.setWidth("200px"));
        ingredientGrid.setHeightByRows(true);
        ingredientGrid.setWidthFull();
        return ingredientGrid;
    }
}

