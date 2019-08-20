package pl.chief.cookbook.gui.layout;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.applayout.MenuItemClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.util.ImagePath;

import java.util.Collection;

public class MenuLayout extends AppLayoutMenu {
    private Collection<SimpleGrantedAuthority> authorities;

    public MenuLayout(AppLayout appLayout) {
        Image logo = new Image(ImagePath.LOGO, "logo");
        logo.setHeight("100px");
        appLayout.setBranding(logo);
        AppLayoutMenu menu = appLayout.createMenu();
        AppLayoutMenuItem logout = new AppLayoutMenuItem(VaadinIcon.EXIT.create(), "logout");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        AppLayoutMenuItem userName = new AppLayoutMenuItem(VaadinIcon.USER.create(), name);
        userName.addMenuItemClickListener(menuItemClick -> {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof User){
                System.out.println(((User)principal).getSurname());
            }
        });
        logout.addMenuItemClickListener(menuItemClickEvent ->
        {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            UI.getCurrent().getPage().reload();
        });
        authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.SEARCH.create(), "Find recipes", ""),
                    new AppLayoutMenuItem(VaadinIcon.ENTER.create(), "Login", "login")
            );
        }

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            menu.addMenuItems(
                    new AppLayoutMenuItem(VaadinIcon.CROSS_CUTLERY.create(), "Manage ingredients", "ingredient-manager"),
                    new AppLayoutMenuItem(VaadinIcon.SITEMAP.create(), "Manage recipes", "recipe-manager"),
                    new AppLayoutMenuItem(VaadinIcon.SEARCH.create(), "Find recipes", ""),
                    logout,
                    userName
            );
        }


    }
}