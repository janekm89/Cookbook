package pl.chief.cookbook.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.gui.views.LoginView;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
        final boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget());
        if (!accessGranted) {
            if (!SecurityUtils.isUserLoggedIn()) {
                event.rerouteTo(LoginView.class);
            }
        }
    }
}