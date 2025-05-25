package pap2024z.z11.Gaming_institution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import pap2024z.z11.Gaming_institution.EmailService.MailManager;


@SpringBootTest
public class TestEmail {
    @Autowired
    private MailManager mailManager;

    @Test
    public void testSendAuthenticationCode() {
        Assert.isTrue(mailManager.sendAuthEmail("tomek.naszkowski@gmail.com","123")==0,"Email was NOT send Successfully");
    }
    @Test
    public void testNewPasswordTest() {
        Assert.isTrue(mailManager.sendNewPassword("tomek.naszkowski@gmail.com","123")==0,"Email was NOT send Successfully");
    }

}
