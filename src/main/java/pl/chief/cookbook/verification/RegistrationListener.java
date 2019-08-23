package pl.chief.cookbook.verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.chief.cookbook.model.User;
import pl.chief.cookbook.service.MailService;
import pl.chief.cookbook.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private UserService userService;
    private MailService mailService;
    private HttpServletRequest request;

    @Autowired
    public RegistrationListener(UserService userService, MailService mailService, HttpServletRequest request) {
        this.userService = userService;
        this.mailService = mailService;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String confirmationUrl
                = request.getRequestURL().toString() + "token/" + token;
        try {
            mailService.sendMail(user.getEmail(), confirmationUrl);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
