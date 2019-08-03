package pl.chief.cookbook.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.service.IngredientService;
import pl.chief.cookbook.util.ImagePath;


@Route("ingredient-manager")
public class IngredientManager extends VerticalLayout {

    private Grid<Ingredient> ingredientGrid;

    @Autowired
    IngredientService ingredientService;

    IngredientManager(IngredientService ingredientService) {
        /*Image img = new Image(
                new StreamResource("log",
                        () -> IngredientManager.class.getResourceAsStream(ImagePath.LOGO)),
                "Cookbook Logo");*/


        Image logo = new Image(ImagePath.LOGO, "logo");
        logo.setHeight("100px");

        AppLayout appLayout = new AppLayout();
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.MENU.create(), "Find recipes", "index")
        );


        TextField nameField = new TextField();
        nameField.setLabel("ingredient name");

        ComboBox<MeasurementUnit> unitComboBox = new ComboBox<>("measurment unit");
        unitComboBox.setItems(MeasurementUnit.values());

        ComboBox<IngredientCategory> ingredientCategoryComboBox = new ComboBox<>("ingredient category");
        ingredientCategoryComboBox.setItems(IngredientCategory.values());


        ingredientGrid = new Grid<>(Ingredient.class);
        ingredientGrid.setItems(ingredientService.findAllIngredients());
        ingredientGrid.removeColumnByKey("recipes");
        ingredientGrid.setColumns("name", "unit", "ingredientCategory");
        ingredientGrid.addColumn(new ComponentRenderer<>(this::buildDeleteButton)).setHeader("remove");
        ingredientGrid.addColumn(new ComponentRenderer<>(this::buildEditButton)).setHeader("edit");
        ingredientGrid.getColumns()
                .forEach(column -> column.setWidth("250px"));
        ingredientGrid.setHeightFull();

        Button button = new Button("Add");

        button.addClickListener(
                buttonClickEvent -> {
                    Ingredient ingredient = new Ingredient();

                    ingredient.setName(nameField.getValue());
                    ingredient.setUnit(unitComboBox.getValue());
                    ingredient.setIngredientCategory(ingredientCategoryComboBox.getValue());

                    ingredientService.addIngredient(ingredient);

                    ingredientGrid.setItems(ingredientService.findAllIngredients());


                    Notification notification = new Notification(
                            "Ingredient sucessfully added to database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();

                });


        HorizontalLayout ingredientEditor = new HorizontalLayout();
        ingredientEditor.add(nameField, unitComboBox, ingredientCategoryComboBox, button);
        ingredientEditor.setVerticalComponentAlignment(Alignment.CENTER, button);

        //    add(ingredientEditor, ingredientGrid);
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(ingredientEditor, ingredientGrid);
        layoutContent.setHeightFull();

        appLayout.setContent(layoutContent);
        add(appLayout);


    }


    private Button buildDeleteButton(Ingredient ingredient) {
        Button button = new Button("remove");

        button.addClickListener(
                buttonClickEvent -> {
                    deleteIngredient(ingredient);
                    Notification notification = new Notification(
                            "Ingredient sucessfully removed from database", 3000,
                            Notification.Position.TOP_START);
                    notification.open();

                });
        return button;
    }


    private void deleteIngredient(Ingredient ingredient) {
        ingredientService.deleteIngredient(ingredient);
        ingredientGrid.setItems(ingredientService.findAllIngredients());
    }

    private Button buildEditButton(Ingredient ingredient) {
        Button button = new Button("edit");

        button.addClickListener(
                buttonClickEvent -> {
                    Dialog dialog = new Dialog();
                    dialog.open();
                    TextField nameField = new TextField();
                    nameField.setLabel("ingredient name");

                    ComboBox<MeasurementUnit> unitComboBox = new ComboBox<>("measurment unit");
                    unitComboBox.setItems(MeasurementUnit.values());

                    ComboBox<IngredientCategory> ingredientCategoryComboBox = new ComboBox<>("ingredient category");
                    ingredientCategoryComboBox.setItems(IngredientCategory.values());

//                    ingredient.setName(nameField.getValue());
//                    ingredient.setUnit(unitComboBox.getValue());
//                    ingredient.setIngredientCategory(ingredientCategoryComboBox.getValue());
//                    dialog.setCloseOnEsc(false);
//                    dialog.setCloseOnOutsideClick(false);

                    Label messageLabel = new Label();

                    NativeButton confirmButton = new NativeButton("Edit ingredient", event -> {

                        ingredient.setName(nameField.getValue());
                        ingredient.setUnit(unitComboBox.getValue());
                        ingredient.setIngredientCategory(ingredientCategoryComboBox.getValue());
                        editIngredient(ingredient);
                        messageLabel.setText("Ingredient edited!");
                        dialog.close();
                    });
                    NativeButton cancelButton = new NativeButton("Cancel", event -> {
                        messageLabel.setText("Cancelled...");
                        dialog.close();
                    });
                    //dialog.add(confirmButton, cancelButton);
                    //dialog.add(nameField, unitComboBox,  ingredientCategoryComboBox, confirmButton, cancelButton);
                    dialog.add(nameField, unitComboBox, ingredientCategoryComboBox, confirmButton, cancelButton);

                });
        return button;
    }

    private void editIngredient(Ingredient ingredient) {
        ingredientService.updateIngredient(ingredient, ingredient.getId());
        ingredientGrid.setItems(ingredientService.findAllIngredients());
    }
}

