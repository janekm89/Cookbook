package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.textfield.NumberField;

public class CaloriesField extends NumberField {

    public CaloriesField() {
        this.setValue(10d);
        this.setMin(0);
        this.setMax(500);
        this.setStep(10);
        this.setHasControls(true);
        this.setLabel("Calories");
    }
}
