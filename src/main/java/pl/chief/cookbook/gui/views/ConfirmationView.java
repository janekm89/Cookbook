package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;
import pl.chief.cookbook.gui.layout.MenuLayout;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.UserService;
import pl.chief.cookbook.verification.VerificationToken;

import java.util.Calendar;

@Route(ConfirmationView.ROUTE)
public class ConfirmationView extends VerticalLayout {
    public final static String ROUTE = "confirmation";
    private UserService userService;

    @Autowired
    public ConfirmationView(WebRequest request, UserService userService) {
        this.userService = userService;
        AppLayout appLayout = new AppLayout();
        new MenuLayout(appLayout);
        VerticalLayout layoutContent = new VerticalLayout();

        System.out.println(Confirmation.token);
        VerificationToken token = userService.getVerificationToken(Confirmation.token);
        if (token == null) {
            System.out.println("Pusty token");
        } else {
            User user = userService.findUserByToken(token.getToken());
            Calendar cal = Calendar.getInstance();
            if ((token.getExpiryDate().getTime() - cal.getTime().getTime() <= 0)) {
                System.out.println("Token wygasł");
                userService.removeUser(user);
            }

            activateUser(user);
            Label label = new Label("Welcome " + user.getUsername());
            layoutContent.add(label);
        }
        layoutContent.setAlignItems(Alignment.CENTER);
        appLayout.setContent(layoutContent);
        add(appLayout);
        UI.getCurrent().getPage().executeJavaScript("window.open(\"/login\", \"_self\")");
    }

    private void activateUser(User user) {
        userService.activateUser(user);
    }

}
