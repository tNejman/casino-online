package pap2024z.z11.Gaming_institution.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap2024z.z11.Gaming_institution.database.Database_manager;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class RestController_Email {

    @Autowired
    private MailManager mailManager;
    @Autowired
    private Database_manager database_manager;

    /**
     * Check a user's resource by UserId and column name.
     *
     * @param sessionId     The ID of the session.
     * @return The value of the specified resource column.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/SendAuthMail")
    public ResponseEntity<String> SendAuthMail(
            @RequestParam String sessionId
    ) throws Exception {
        int Session_Id = Integer.parseInt(sessionId);
        int user_id = database_manager.GetUserSessionId(Session_Id);
        String Email = database_manager.CheckUserResources(user_id,"Email");
        mailManager.sendAuthEmail(Email,Integer.toString(Session_Id));
        return ResponseEntity.ok(sessionId);
    }
    @PostMapping("/SendPassMail")
    public ResponseEntity<String> SendPassMail(
            @RequestParam String email
    ) throws Exception {
        String NewPassword = database_manager.MakeNewPassword(email).getFirst();
        mailManager.sendNewPassword(email,NewPassword);
        return ResponseEntity.ok(NewPassword);
    }
}