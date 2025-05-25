package pap2024z.z11.Gaming_institution.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;

@RestController
public class MailManager {

    @Autowired
    private EmailService emailService;

    public Integer sendAuthEmail(String email, String AuthenticationCode) {
        try {
            String body = "<h1>Verify Your Email</h1><p> here is your authentication code: <strong>"+AuthenticationCode+"</strong></p>";
            emailService.sendEmail(
                    email,
                    "Authentication Required",
                    body
            );
            return 0;
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            return -1;
        }
    }

    public Integer sendNewPassword(String email, String Password) {
        try {
            String body = "<h1>Your New Password</h1><p> here is your new Password: <strong>" + Password + "</strong> </p>";
            emailService.sendEmail(
                    email,
                    "Password Reset",
                    body
            );
            return 0;
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            return -1;
        }
    }
}