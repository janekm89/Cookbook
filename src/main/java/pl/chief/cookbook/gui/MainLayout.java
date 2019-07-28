package pl.chief.cookbook.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import pl.chief.cookbook.util.AppProperties;

@Route("index")
public class MainLayout extends VerticalLayout {
    private static final String LOGO_PNG = AppProperties.logo;

    MainLayout() {
        /*Image img = new Image(
                new StreamResource(LOGO_PNG,
                        () -> MainLayout.class.getResourceAsStream("\\src\\main\\resources\\img\\logo.png")),
                "Cookbook Logo"
        );*/

        Image img = new Image("https://i.kinja-img.com/gawker-media/image/upload/s--eZrGJpKG--/c_scale,f_auto,fl_progressive,q_80,w_800/mfsptkstqthdh8xrdydn.jpg", "erro");
        img.setHeight("44px");


        AppLayout appLayout = new AppLayout();
        AppLayoutMenu menu = appLayout.createMenu();

        appLayout.setBranding(img);


        menu.addMenuItems(new AppLayoutMenuItem("Page 1", "page1"),
                new AppLayoutMenuItem("Page 2", "page2"),
                new AppLayoutMenuItem("Page 3", "page3"),
                new AppLayoutMenuItem("Page 4", "page4"),
                new AppLayoutMenuItem(VaadinIcon.USER.create(), "My Profile", "profile"),
                new AppLayoutMenuItem(VaadinIcon.TRENDING_UP.create(), "Trending Topics", "trends")
        );


        Component content = new Span(new H3("Page title"),
                new Span("Page content"));
        appLayout.setContent(content);

        add(appLayout);

    }

}