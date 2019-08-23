package pl.chief.cookbook.gui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(Confirmation.ROUTE)
public class Confirmation extends Div implements HasUrlParameter<String> {
    public final static String ROUTE = "token";
    public static String token;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String message) {
        if (message == null) {
            event.forwardTo(LoginView.class);
        } else {
            Confirmation.token = message;
            System.out.println(token);
            event.forwardTo(ConfirmationView.class);
        }

    }

}
