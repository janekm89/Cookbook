package pl.chief.cookbook.gui.views;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.WebRequest;
import pl.chief.cookbook.builder.UserBuilder;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.MailService;
import pl.chief.cookbook.service.UserService;
import pl.chief.cookbook.verification.OnRegistrationCompleteEvent;


@Route(Registration.ROUTE)
public class Registration extends VerticalLayout {
    public final static String ROUTE = "register";

    private ApplicationEventPublisher eventPublisher;
    private UserService userService;
    private MailService mailService;
    private TextField usernameField;
    private TextField nameField;
    private TextField surnameField;
    private EmailField emailField;
    private EmailField emailConfirmField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmField;

    @Autowired
    public Registration(UserService userService, MailService mailService, ApplicationEventPublisher eventPublisher, WebRequest request) {
        this.userService = userService;
        this.mailService = mailService;
        this.eventPublisher = eventPublisher;
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout layoutContent = setRegisterLayoutContent(request);


        layoutContent.setAlignItems(Alignment.CENTER);
        appLayout.setContent(layoutContent);
        add(appLayout);
    }

    private VerticalLayout setRegisterLayoutContent(WebRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        setRegisterFields();
        verticalLayout.add(userNameLayout());
        verticalLayout.add(namesLayout());
        verticalLayout.add(emailLayout());
        verticalLayout.add(passwordLayout());
        verticalLayout.add(registerButton(request));
        return verticalLayout;
    }

    private Button registerButton(WebRequest request) {
        Button button = new Button("Register");
        button.addClickShortcut(Key.ENTER);
        button.setAutofocus(true);
        button.addClickListener(click -> {
            if (passwordField.getValue().equals(passwordConfirmField.getValue())
                    && emailField.getValue().equals(emailField.getValue())) {
                User user = new UserBuilder().withUsername(usernameField.getValue())
                        .withName(nameField.getValue())
                        .withSurname(surnameField.getValue())
                        .withEmail(emailField.getValue())
                        .withPassword(passwordField.getValue())
                        .withRoleUser()
                        .unactive().create();
                try {
                    userService.addUser(user);
                } catch (EntityAlreadyExistException e) {
                    e.printStackTrace();
                }
                String appUrl = request.getContextPath();
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
                Dialog dialog = buildCreatorDialog();
                dialog.open();
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

    private Dialog buildCreatorDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        Label label = new Label("Check you email for activation link");
        verticalLayout.add(label, buildOKButton(dialog));
        verticalLayout.setAlignItems(Alignment.CENTER);
        dialog.add(verticalLayout);
        return dialog;
    }

    private Button buildOKButton(Dialog dialog) {
        Button button = new Button("OK");
        button.setIcon(IronIcons.CANCEL.create());
        button.addClickListener(click -> {
            dialog.close();
            UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\")");
        });
        return button;
    }
}
