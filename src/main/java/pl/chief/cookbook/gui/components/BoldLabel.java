package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.html.Label;

public class BoldLabel extends Label {
    public BoldLabel(String text) {
        super(text);
        this.getStyle().set("font-weight", "bold");
    }
}
