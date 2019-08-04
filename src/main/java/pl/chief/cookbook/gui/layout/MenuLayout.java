package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import pl.chief.cookbook.util.ImagePath;

public class MenuLayout extends AppLayoutMenu {

    public MenuLayout(AppLayout appLayout) {
        Image logo = new Image(ImagePath.LOGO, "logo");
        logo.setHeight("100px");
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                new AppLayoutMenuItem(VaadinIcon.SEARCH.create(), "Find recipes", "")
        );
    }
}
