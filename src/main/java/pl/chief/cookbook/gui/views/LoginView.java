package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.chief.cookbook.gui.layout.MenuLayout;

import java.util.Collections;

@Tag("sa-login-view")
@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginForm loginForm = new LoginForm();

    public LoginView() {
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout content = new VerticalLayout();

        loginForm.setAction("login");
        getElement().appendChild(loginForm.getElement());
        content.add(loginForm);
        content.setAlignItems(Alignment.CENTER);
        appLayout.setContent(content);
        add(appLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        // (yes, the API for resolving query parameters is annoying...)
        if(!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
            loginForm.setError(true);
        }
    }
    }

