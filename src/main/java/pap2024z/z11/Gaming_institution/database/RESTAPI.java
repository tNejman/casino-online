package pap2024z.z11.Gaming_institution.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class RESTAPI {

    @Autowired
    private Database_manager database_manager;

    /**
     * Check a user's resource by UserId and column name.
     *
     * @param Login     The ID of the user.
     * @param ColumnName The name of the column to check.
     * @return The value of the specified resource column.
     * @throws Exception If an error occurs.
     */
    // @GetMapping("/checkResources")
    // public ResponseEntity<String> checkUsersResource(
    //         @RequestParam String Login,
    //         @RequestParam String ColumnName
    // ) throws Exception {
    //     return ResponseEntity.ok(database_manager.CheckUserResources(Login, ColumnName));
    // }
    @GetMapping("/checkResources/{Login}/{ColumnName}")
    public ResponseEntity<String> checkUsersResource(
            @PathVariable String Login,
            @PathVariable String ColumnName
    ) throws Exception {
        try {
            int userId = Integer.parseInt(Login);
            return ResponseEntity.ok(database_manager.CheckUserResources(userId, ColumnName));
        } catch (Exception e)
        {
            return ResponseEntity.ok(database_manager.CheckUserResources(Login, ColumnName));
        }
    }
    /**
     * Check a user's resource by UserId and column name.
     *
     * @param SessionId  get session_id
     * @return UserId
     * @throws Exception If an error occurs.
     */
    @GetMapping("/checkUserSessionId")
    public ResponseEntity<Integer> checkUserSessionId(
            @RequestParam int SessionId
    ) throws Exception {
        return ResponseEntity.ok(database_manager.GetUserSessionId(SessionId));
    }
    /**
     * Add a new user to the system.
     *
     * @param Login    The login name of the user.
     * @param Password The user's password.
     * @param Email    The user's email.
     * @param Age      The user's age.
     * @return The ID of the newly created user.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/adding_users")
    public ResponseEntity<Integer> AddUser(
            @RequestParam String Login,
            @RequestParam String Password,
            @RequestParam String Email,
            @RequestParam String Age
    ) throws Exception {
        return ResponseEntity.ok(database_manager.adding_user(Login, Password, Email, Age));
    }

    /**
     * Log in a user and start a session.
     *
     * @param Login    The user's login name.
     * @param Password The user's password.
     * @return List of parameters of user in this order
     * "UserId"
     * "LoginName"
     * "MoneyAmount"
     * "LoanAmount"
     * "LevelAmount"
     * "Email"
     * "Age"
     * "Session_id"
     * @throws Exception If an error occurs.
     */
    @PostMapping("/login")
    public ResponseEntity<List<String>> logInUser(
            @RequestParam String Login,
            @RequestParam String Password
    ) throws Exception {
        return ResponseEntity.ok(database_manager.login_in_user(Login, Password));
    }

    /**
     * Add money to a user's account.
     *
     * @param Login The user's Login.
     * @param Money  The amount of money to add.
     * @return The new balance.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/addMoney")
    public ResponseEntity<String> addMoney(
            @RequestParam String Login,
            @RequestParam String Money
    ) throws Exception {
        return ResponseEntity.ok(Integer.toString(database_manager.AddMoney(Login,Integer.parseInt(Money))));
    }

    /**
     * Set money to a user's account.
     *
     * @param Login The user's Login.
     * @param Money  The amount of money to set to.
     * @return The new balance.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/setMoney")
    public ResponseEntity<String> SetMoney(
            @RequestParam String Login,
            @RequestParam String Money
    ) throws Exception {
        return ResponseEntity.ok(Integer.toString(database_manager.SetMoney(Login, Integer.parseInt(Money))));
    }
    /**
     * Set email to a user's account.
     *
     * @param Login The user's Login.
     * @param Email  The amount of money to set to.
     * @return The new balance.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/setEmail")
    public ResponseEntity<String> SetEmail(
            @RequestParam String Login,
            @RequestParam String Email
    ) throws Exception {
        return ResponseEntity.ok(database_manager.SetEmail(Login, Email));
    }
    /**
     * Set login to a user's account.
     *
     * @param Login The user's Login.
     * @param NewLogin  The amount of money to set to.
     * @return The new balance.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/setNewLogin")
    public ResponseEntity<String> SetNewLogin(
            @RequestParam String Login,
            @RequestParam String NewLogin
    ) throws Exception {
        return ResponseEntity.ok(database_manager.SetLogin(Login, NewLogin));
    }
    /**
     * Add a loan to a user's account.
     *
     * @param UserId The user's ID.
     * @param Loan   The amount of the loan.
     * @return The new loan balance.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/addLoan")
    public ResponseEntity<Integer> addLoan(
            @RequestParam int UserId,
            @RequestParam int Loan
    ) throws Exception {
        return ResponseEntity.ok(database_manager.AddLoan(UserId, Loan));
    }

    /**
     * Add experience points to a user.
     *
     * @param UserId     The user's ID.
     * @param Experience The amount of experience to add.
     * @return The new total experience.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/addExperience")
    public ResponseEntity<Integer> addExperience(
            @RequestParam int UserId,
            @RequestParam int Experience
    ) throws Exception {
        return ResponseEntity.ok(database_manager.AddExperience(UserId, Experience));
    }

    /**
     * End a user's session (log out).
     *
     * @param UserId The user's ID.
     * @return A status code indicating success (1) or failure (-1 or -2).
     * @throws Exception If an error occurs.
     */
    @PostMapping("/endSession")
    public ResponseEntity<Integer> endSession(
            @RequestParam String UserId
    ) throws Exception {
        return ResponseEntity.ok(database_manager.EndSession(Integer.parseInt(UserId)));
    }

    /**
     * Delete a user by login name.
     *
     * @param Login The user's login name.
     * @return A boolean indicating whether the user was deleted successfully.
     * @throws Exception If an error occurs.
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Boolean> deleteUser(
            @RequestParam String Login
    ) throws Exception {
        return ResponseEntity.ok(database_manager.DeleteUser(Login));
    }

    /**
     * Change a user's password.
     *
     * @param Login      The user's ID.
     * @param OldPassword The user's current password.
     * @param NewPassword The new password to set.
     * @return A boolean indicating whether the password was changed successfully.
     * @throws Exception If an error occurs.
     */
    @PostMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(
            @RequestParam String Login,
            @RequestParam String OldPassword,
            @RequestParam String NewPassword
    ) throws Exception {
        return ResponseEntity.ok(database_manager.ChangePassword(Login, OldPassword, NewPassword));
    }
    /**
     * Make new password for user knowing Email
     *
     * @param Email      The user's ID.
     * @return unhashed password
     * @return hashed password
     * @throws Exception If an error occurs.
     */
    @PostMapping("/MakeNewPassword")
    public ResponseEntity<List<String>> MakeNewPassword(
            @RequestParam String Email
    ) throws Exception {
        return ResponseEntity.ok(database_manager.MakeNewPassword(Email));
    }
    /**
     * Make new password for user knowing Email
     *
     * @return Login and moneyAmount of every player
     * @throws Exception If an error occurs.
     */
    @PostMapping("/GetAllPlayers")
    public ResponseEntity<List<List<String>>> GetAllPlayers() throws Exception {
        return ResponseEntity.ok(database_manager.GetEveryPlayer());
    }
    @PostMapping("/admin/GetNPlayers")
    public ResponseEntity<List<List<String>>> GetNPlayers(@RequestParam String N) throws Exception {
        return ResponseEntity.ok(database_manager.GetNPlayer(Integer.parseInt(N)));
    }
    @PostMapping("/admin/getPlayerCountOverTime")
    public ResponseEntity<List<List<String>>> GetPlayerCountOverTime() throws Exception {
        return ResponseEntity.ok(database_manager.GetPlayerCountOverTime());
    }
}