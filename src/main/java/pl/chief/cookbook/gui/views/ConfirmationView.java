package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.UserService;
import pl.chief.cookbook.verification.VerificationToken;

import java.util.Calendar;

@Route(ConfirmationView.ROUTE)
public class ConfirmationView extends VerticalLayout {
    public final static String ROUTE = "confirmation";
    private UserService userService;

    private Label informationLabel;

    @Autowired
    public ConfirmationView(UserService userService) {
        this.userService = userService;
        informationLabel = new Label();
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.add(informationLabel);
        System.out.println(Confirmation.token);
        VerificationToken token = userService.getVerificationToken(Confirmation.token);
        if (token == null) {
            informationLabel.setText("The activation token is empty");
        } else {
            User user = userService.findUserByToken(token.getToken());
            Calendar cal = Calendar.getInstance();
            if ((token.getExpiryDate().getTime() - cal.getTime().getTime() <= 0)) {
                informationLabel.setText("Sorry " + user.getUsername() + " your token has expired. Your account will be deleted.");
                userService.removeUser(user);
            }

            activateUser(user);
            informationLabel.setText("Welcome " + user.getUsername() + " your account is now active");
            Button button = new Button("Login");
            button.addClickListener(click->{
                UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\")");
            });
            layoutContent.add(button);
        }
        layoutContent.setAlignItems(Alignment.CENTER);
        appLayout.setContent(layoutContent);
        add(appLayout);
    }

    private void activateUser(User user) {
        userService.activateUser(user);
    }

}
