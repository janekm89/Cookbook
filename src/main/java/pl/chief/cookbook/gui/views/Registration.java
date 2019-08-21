package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.builder.UserBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.UserService;



@Route(Registration.ROUTE)
public class Registration extends VerticalLayout {
    public final static String ROUTE = "register";
    private UserService userService;
    private TextField usernameField;
    private TextField nameField;
    private TextField surnameField;
    private EmailField emailField;
    private EmailField emailConfirmField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;

    @Autowired
    public Registration(UserService userService) {
        this.userService = userService;
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout layoutContent = setRegisterLayoutContent();


        layoutContent.setAlignItems(Alignment.CENTER);
        appLayout.setContent(layoutContent);
        add(appLayout);
    }

    private VerticalLayout setRegisterLayoutContent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        setRegisterFields();
        verticalLayout.add(userNameLayout());
        verticalLayout.add(namesLayout());
        verticalLayout.add(emailLayout());
        verticalLayout.add(passwordLayout());
        verticalLayout.add(registerButton());
        return verticalLayout;
    }

    private Button registerButton() {
        Button button = new Button("Register");
        button.addClickShortcut(Key.ENTER);
        button.setAutofocus(true);
        button.addClickListener(click -> {
            if(passwordField.getValue().equals(passwordConfirmField.getValue())
                    && emailField.getValue().equals(emailField.getValue())) {
                User user = new UserBuilder().withUsername(usernameField.getValue())
                        .withName(nameField.getValue())
                        .withSurname(surnameField.getValue())
                        .withEmail(emailField.getValue())
                        .withPassword(passwordField.getValue())
                        .withRoleUser()
                        .activated().create();
                try {
                    userService.addUser(user);
                } catch (EntityAlreadyExistException e) {
                    e.printStackTrace();
                }
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\")");
            }

        });
        return button;
    }

    private void setRegisterFields() {
        usernameField = new TextField("Username:");
        nameField = new TextField("Name:");
        surnameField = new TextField("Surname:");
        emailField = new EmailField("E-mail:");
        emailConfirmField = new EmailField("Confirm E-mail:");
        passwordField = new PasswordField("Password:");
        passwordConfirmField = new PasswordField("Confirm password:");
    }

    private HorizontalLayout userNameLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(usernameField);
        return horizontalLayout;
    }

    private HorizontalLayout namesLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameField, surnameField);
        return horizontalLayout;
    }

    private HorizontalLayout emailLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(emailField, emailConfirmField);
        return horizontalLayout;
    }

    private HorizontalLayout passwordLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(passwordField, passwordConfirmField);
        return horizontalLayout;
    }
}
