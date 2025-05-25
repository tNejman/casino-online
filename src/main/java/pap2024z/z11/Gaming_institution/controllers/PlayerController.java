package pap2024z.z11.Gaming_institution.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.net.http.HttpRequest;

import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import pap2024z.z11.Gaming_institution.EmailService.MailManager;
import pap2024z.z11.Gaming_institution.gamelogic.Player;
import pap2024z.z11.Gaming_institution.game.Roulette;

import com.fasterxml.jackson.databind.ObjectMapper;

import pap2024z.z11.Gaming_institution.exceptions.EmptyNameException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalBetValueException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalPocketNumberException;
import pap2024z.z11.Gaming_institution.game.Roulette.Bets;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/player")
public class PlayerController {

    // Maybe there is better solution, but for now player's name
    // is key which returns player object
    // every name has to be unique
    public static final Map<String, Player> players = new HashMap<>();

    private final ParameterizedTypeReference<Integer> integerType = new ParameterizedTypeReference<Integer>() {};
    private final ParameterizedTypeReference<String> StringType = new ParameterizedTypeReference<String>() {};
    private final ParameterizedTypeReference<Boolean> BooleanType = new ParameterizedTypeReference<Boolean>() {};
    private final ParameterizedTypeReference<List<String>> listStringType = new ParameterizedTypeReference<List<String>>() {};
    private final ParameterizedTypeReference<List<List<String>>> doubleListStringType = new ParameterizedTypeReference<List<List<String>>>() {};
    
    private final RestTemplate restTemplate;
    @SuppressWarnings("unused")
    @Autowired
    private MailManager mailManager;
    public PlayerController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private <T> ResponseEntity<T> callDbApi(String url, MultiValueMap<String,String> paramMap, ParameterizedTypeReference<T> returnType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                returnType
        );

        return responseEntity;
    }
    private <T> ResponseEntity<T> callDbApiGet(String url, MultiValueMap<String,String> paramMap, ParameterizedTypeReference<T> returnType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                returnType
        );

        return responseEntity;
    }

    private <T> ResponseEntity<T> callDbApiDelete(String url, MultiValueMap<String,String> paramMap, ParameterizedTypeReference<T> returnType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                returnType
        );

        return responseEntity;
    }

    @GetMapping("/getAllPlayers")
    public ResponseEntity<?> getAllPlayers() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        List<List<String>> playerScoreList;
        try {
        ResponseEntity<List<List<String>>> responseEntity = this.callDbApi(
            "http://localhost:8081/api/users/GetAllPlayers",
            map,
            doubleListStringType);

        playerScoreList = responseEntity.getBody();

        return ResponseEntity.ok(playerScoreList);

        } catch (Exception e) {
            // List<List<String>> list = new ArrayList<>();
            // List<String> list2 = new ArrayList<>();
            // list2.add("-1");
            // list2.add("-1");
            // list.add(list2);
            // return ResponseEntity.badRequest().body(list);
            return ResponseEntity.badRequest().body("Error while loading all players: " + e.getMessage());
        }
    }
    // n
    // (n*5) (n*5)+1 (n*5)+2 (n*5)+3 (n*5) + 4

    @PostMapping("/admin/getNPlayers/{n}")
    public ResponseEntity<?> adminGetNPlayers(@PathVariable String n) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("N", n);
        List<List<String>> playerData;
        try {
            ResponseEntity<List<List<String>>> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/admin/GetNPlayers",
                    map,
                    doubleListStringType);
            playerData = responseEntity.getBody();
            
            if (playerData == null) {
                throw new IllegalStateException("PlayerData is null");
            // } else if (playerData.size() == 0) {
                // throw new IllegalStateException("No players found in database");
            }
            // System.out.println(playerData);
            return ResponseEntity.ok(playerData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while loading all players: " + e.getMessage());
        }
    }

    @PostMapping("/admin/getPlayerCountOverTime")
    public ResponseEntity<?> getPlayerCountOverTime() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        List<List<String>> playerData;
        try {
            ResponseEntity<List<List<String>>> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/admin/getPlayerCountOverTime",
                    map,
                    doubleListStringType);
            playerData = responseEntity.getBody();

            if (playerData == null) {
                throw new IllegalStateException("PlayerData is null");
                // } else if (playerData.size() == 0) {
                // throw new IllegalStateException("No players found in database");
            }
            // System.out.println(playerData);
            return ResponseEntity.ok(playerData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while loading all players: " + e.getMessage());
        }
    }


    // PLAYER INIT
    // (adds player to list)
    @PostMapping("/create")
    public ResponseEntity<String> createPlayer(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String password = request.get("password");
        String email = request.get("email");
        int age;
        Integer userId;

        String[] params = {name, password, email};
        for (String param : params) {
            if (param == null || param.isEmpty()) {
                return ResponseEntity.badRequest().body(param + " required");
            }
        }
        try {
            age = Integer.valueOf(request.get("age"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid age format!");
        }
        if (players.containsKey(name)) {
            return ResponseEntity.badRequest().body("Player already exists!");
        }
        try {
            // send request to create player in BD
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", name);
            map.add("Password", password);
            map.add("Email", email);
            map.add("Age", String.valueOf(age));

            ResponseEntity<Integer> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/adding_users",
                    map,
                    integerType
            );

            if (responseEntity.getBody() == null) {
                return ResponseEntity.badRequest().body("Failed to create player in DB: DB returned null");
            }

            userId = responseEntity.getBody();


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        try {
            // Actually create player
            @SuppressWarnings("null")
            Player newPlayer = new Player(userId, name, password, email, age);
            players.put(name, newPlayer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed player creation. Error status: " + e.getMessage());
        }
        return ResponseEntity.ok("Player " + name + " created successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPlayer(@RequestBody Map<String, String> request) {
        for (Player p : players.values()) {
            p.setSessionId(-1);
            p.leaveGame();
        }
        RouletteController.roulette = new Roulette();
        String login = request.get("login");
        String password = request.get("password");
        int sessionId = -3;
        int money;
        List<String> userData;
        Player player;
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if ( login == null || password == null || login.isEmpty() || password.isEmpty() ) {
            return ResponseEntity.badRequest().body(-1);
        }

        try {
            // send request to login player in BD
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", login);
            map.add("Password", password);

            ResponseEntity<List<String>> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/login",
                    map,
                    listStringType
            );
            userData = responseEntity.getBody();
            if (userData == null) {
                return ResponseEntity.badRequest().body(-1);
            }
            sessionId = Integer.parseInt(userData.getLast());
            boolean ifFound = false;
            for (String key : players.keySet()) {
                if (players.get(key).getName().equals(login)) {
                    ifFound = true;
                    player = players.get(key);
                    RouletteController.roulette.setPlayers(player);
                    player.setSessionId(sessionId);
                    break;
                }
            } 
            if (!ifFound) {
                int userId = Integer.parseInt(userData.getFirst());
                login = userData.get(1);
                money = Integer.parseInt(userData.get(2));
                String email = userData.get(5);
                int age = Integer.parseInt(userData.get(6));
                Player newPlayer = new Player(userId, login, password, email, age);
                newPlayer.setMoney(money);
                newPlayer.setSessionId(sessionId);
                players.put(login, newPlayer);
                RouletteController.roulette.setPlayers(newPlayer);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(sessionId);
    }

    @DeleteMapping("/{name}/delete")
    public ResponseEntity<?> deletePlayer(@PathVariable String name) {
        try {
            // send request to delete player from BD
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", name);
            ResponseEntity<Integer> responseEntity = this.callDbApiDelete(
                    "http://localhost:8081/api/users/deleteUser",
                    map,
                    integerType
            );
        } catch (Exception e) {
            return ResponseEntity.ok("Player " + name + " deleted successfully.");
        }
        return ResponseEntity.ok("Player " + name + " deleted successfully.");
    }

    // @PostMapping("{name}/update")
    // public ResponseEntity<String> updatePlayer(@RequestBody Map<String, String> request) {
    //     String userId = request.get("userId");
    //     String name = request.get("name");
    //     String password = request.get("password");
    //     String email = request.get("email");
    //     String age = request.get("age");
    //     String money = request.get("money");

    //     String[] params = {userId, name, password, email, age, money};
    //     for ( String param : params) {
    //         if (param == null || param.isEmpty()) {
    //             return ResponseEntity.badRequest().body(param + " is required");
    //         }
    //     }

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    //     map.add("Login", name);
    //     map.add("Password", password);
    //     map.add("Email", email);
    //     map.add("Age", String.valueOf(age));

    //     HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

    //     ResponseEntity<Integer> responseEntity = restTemplate.exchange(
    //             "http://localhost:8081/api/users/adding_users",
    //             HttpMethod.POST,
    //             requestEntity,
    //             Integer.class
    //     );
    // }

    // add money
    @PostMapping("/{Login}/money/add/{money}")
    public ResponseEntity<String> addPlayerMoney(@PathVariable String Login, @PathVariable String money) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", Login);
            map.add("Money", money);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/addMoney",
                    map,
                    StringType
            );
            money = responseEntity.getBody();
            return ResponseEntity.ok(money);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/sendAuthenticationEmail/{sessionId}")
    public ResponseEntity<String> sendAuthenticationEmail(@PathVariable String sessionId) {

        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("sessionId", sessionId);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/email/SendAuthMail",
                    map,
                    StringType
            );

            sessionId = responseEntity.getBody();
            return ResponseEntity.ok(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error at auth sending" + e.getMessage());
        }
    }
    @PostMapping("/sendResetPasswordEmail/{email}")
    public ResponseEntity<String> sendNewPasswordEmail(@PathVariable String email) {

        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("email", email);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/email/SendPassMail",
                    map,
                    StringType
            );

            String response = responseEntity.getBody();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error at new password sending" + e.getMessage());
        }
    }

    @PostMapping("/endSession/{SessionId}")
    public ResponseEntity<String> endSession(@PathVariable String SessionId) {
        String retSessionId = "-2";
        try {
            String UserId = "-1";
            for (Player p : players.values()) {
                p.setSessionId(-1);
                p.leaveGame();
            }
            RouletteController.roulette = new Roulette();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("UserId", UserId);
            ResponseEntity<Integer> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/endSession",
                    map,
                    integerType
            );
            if (responseEntity.getBody() == null) {
                return ResponseEntity.badRequest().body("DB returned null response while ending seesion");
            } else {
                retSessionId = Integer.toString(responseEntity.getBody());
            }


            return ResponseEntity.ok("Ended session of id: " + retSessionId + " of user: " + UserId);
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Failed endSession. Error status: " + e.getMessage());
        }
    }

    @PostMapping("{name}/password/set/{oldPassword}/{newPassword}")
    public ResponseEntity<Boolean> setPassword(@PathVariable String name, @PathVariable String oldPassword, @PathVariable String newPassword) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", name);
            map.add("OldPassword", oldPassword);
            map.add("NewPassword", newPassword);
            ResponseEntity<Boolean> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/changePassword",
                    map,
                    BooleanType
            );
            if (responseEntity.getBody() == null) {
                throw new IllegalStateException("Null in database.");
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    /*
     * =======================================================================
     * SETTERS
     * =======================================================================
     */


    // set money
    // if given data is incorrect, for example empty name
    // very long error log appears, it should be fixed i guess
    @PostMapping("/{Login}/money/set/{money}")
    public ResponseEntity<?> setMoney(@PathVariable String Login, @PathVariable String money) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", Login);
            map.add("Money", money);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/setMoney",
                    map,
                    StringType
            );

            money = String.valueOf(responseEntity.getBody());
            return ResponseEntity.ok(money);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1);
        }
    }
    @PostMapping("/{Login}/email/set/{email}")
    public ResponseEntity<String> setEmail(@PathVariable String Login, @PathVariable String email) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", Login);
            map.add("Email", email);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/setEmail",
                    map,
                    StringType
            );

            email = responseEntity.getBody();
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{Login}/name/set/{NewLogin}")
    public ResponseEntity<String> setNewLogin(@PathVariable String Login, @PathVariable String NewLogin) {
        try {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", Login);
            map.add("NewLogin", NewLogin);
            ResponseEntity<String> responseEntity = this.callDbApi(
                    "http://localhost:8081/api/users/setNewLogin",
                    map,
                    StringType
            );
            Player p = players.get(Login);
            p.setName(NewLogin);
            players.remove(Login);
            players.put(NewLogin, p);
            NewLogin = responseEntity.getBody();
            System.out.println(NewLogin);
            NewLogin = responseEntity.getBody();
            return ResponseEntity.ok(NewLogin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{name}/in-game/set")
    public ResponseEntity<?> setIfInGame(@PathVariable String name, @RequestParam boolean ifInGame) {
        try {
            Player player = players.get(name);
            if (player == null) {
                return ResponseEntity.badRequest().body("Player not found!");
            }
            player.setIfInGame(ifInGame);
            return ResponseEntity.ok("Player " + name + " in-game status set to: " + ifInGame);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{name}/ready/set")
    public ResponseEntity<?> setIfReady(@PathVariable String name, @RequestParam boolean ifReady) {
        try {
            Player player = players.get(name);
            if (player == null) {
                return ResponseEntity.badRequest().body("Player not found!");
            }
            player.setIfReady(ifReady);
            return ResponseEntity.ok("Player " + name + " ready status set to: " + ifReady);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * =======================================================================
     * GETTERS
     * =======================================================================
     */

    // get player
    @GetMapping("/{name}")
    public ResponseEntity<?> getPlayer(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player);
    }

    // get money
    @GetMapping("/{name}/money/get")
    public ResponseEntity<?> getPlayerMoney(@PathVariable String name) {
        try {
            MultiValueMap<String,String> map = new LinkedMultiValueMap<String, String>();
            // map.add("Login", name);
            // map.add("ColumnName", "MoneyAmount");
            ResponseEntity<String> responseEntity = callDbApiGet("http://localhost:8081/api/users/checkResources/" +name +"/MoneyAmount", map, StringType);
            return ResponseEntity.ok(responseEntity.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get email
    @GetMapping("/{name}/email/get")
    public ResponseEntity<?> getEmail(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getEmail());
    }


    // get hand
    @GetMapping("/{name}/hand/get")
    public ResponseEntity<?> getHand(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getHand());
    }

    // get hand size
    @GetMapping("/{name}/hand/get/size")
    public ResponseEntity<?> getHandSize(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getHandSize());
    }

    // get if hand Empty
    @GetMapping("/{name}/isHandEmpty")
    public ResponseEntity<?> getIfHandEmpty(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getIfHandEmpty());
    }

    // get if in game
    @GetMapping("/{name}/isInGame")
    public ResponseEntity<?> getIfInGame(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getIfInGame());
    }

    // get if ready
    @GetMapping("/{name}/isReady")
    public ResponseEntity<?> getIfReady(@PathVariable String name) {
        Player player = players.get(name);
        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found.");
        }
        return ResponseEntity.ok(player.getIfReady());
    }

    // join game and leave game imo are not that usefull for now, but i can add them
    // later

    // there are also cardgame methods: playCard, drawCard, sortHandBySuit,
    // sortHandByRank and setHand, which are not implemented yet


}


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/roulette")
class RouletteController {

    public static Roulette roulette = new Roulette();
    private final String playerBaseUrl = "http://localhost:8081/api/player";

    private final ParameterizedTypeReference<Integer> integerType = new ParameterizedTypeReference<Integer>() {};
    private final ParameterizedTypeReference<List<String>> listStringType = new ParameterizedTypeReference<List<String>>() {};
    private final ParameterizedTypeReference<String> StringType = new ParameterizedTypeReference<String>() {};
    private final RestTemplate restTemplate;

    public RouletteController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private <T> ResponseEntity<T> callDbApi(String url, MultiValueMap<String,String> paramMap, ParameterizedTypeReference<T> returnType) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                returnType
        );

        return responseEntity;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetRoulette() {
        this.roulette = new Roulette();
        return ResponseEntity.ok("Roulette created/reset successfully.");
    }

    // spin
    @PostMapping("/spin")
    public ResponseEntity<String> spinRoulette() {
        roulette.spin();
        return ResponseEntity.ok(Integer.toString(roulette.getWinningPocket()));
    }

    // place bet
    @PostMapping("/bet/place/{betName}")
    public ResponseEntity<?> roulettePlaceBet(@PathVariable String betName) {
        try {
            Bets bet = Roulette.getBet(betName.toUpperCase());
            roulette.placeBet(bet, roulette.getSelectedPockets(), roulette.getBetAmount());
            String ms = String.valueOf(roulette.getBetAmount());
            String ms2 = roulette.getBet().name();
            return ResponseEntity.ok("Bet placed successfully! Bet amount: " + ms + " bet: " + ms2 + " players in game: " + String.valueOf(roulette.getPlayerCount()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * =======================================================================
     * SETTERS
     * =======================================================================
     */

    // set name
    @PostMapping("/name/set/{name}")
    public ResponseEntity<?> setRouletteName(@PathVariable String name) {
        try {
            roulette.setName(name);
            return ResponseEntity.ok("Roulette name set to: " + name);
        } catch (EmptyNameException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // set money
    @PostMapping("/player/{name}/money/set")
    public ResponseEntity<?> setPlayerMoney(@PathVariable String name, @RequestParam int amount) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required!");
            }

            Player player = findPlayerByName(roulette.getPlayers(), name);
            if (player == null) {
                return ResponseEntity.badRequest().body("Player not found!");
            }
            roulette.setPlayerMoney(player, amount);
            return ResponseEntity.ok(player.getName() + "'s balance: " + amount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // add money
    @PostMapping("/player/{name}/money/add")
    public ResponseEntity<?> addPlayerMoney(@PathVariable String name, @RequestParam int amount) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required!");
            }

            return ResponseEntity.ok("Successfully added: " + amount + " to " + name + "'s account!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add/player/{name}")
    public ResponseEntity<?> addPlayerToRoulette(@PathVariable String name) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required!");
            }

            Player newPlayer = getPlayerFromPlayerController(name);

            boolean playerExists = roulette.getPlayers().stream()
                    .anyMatch(player -> player.getName().equals(newPlayer.getName()));
            if (playerExists) {
                return ResponseEntity.badRequest().body("Player already exists in the roulette!");
            }

            if (newPlayer == null) {
                return ResponseEntity.badRequest().body("Player not found!");
            }

            roulette.addPlayer(newPlayer);
            return ResponseEntity.ok("Successfully added: " + newPlayer.getName() + " to roulette!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/set/player/{name}")
    public ResponseEntity<?> setPlayerToRoulette(@PathVariable String name) {
        try {
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required!");
            }

            Player newPlayer = getPlayerFromPlayerController(name);

            boolean playerExists = roulette.getPlayers().stream()
                    .anyMatch(player -> player.getName().equals(newPlayer.getName()));
            if (playerExists) {
                return ResponseEntity.badRequest().body("Player already exists in the roulette!");
            }

            if (newPlayer == null) {
                return ResponseEntity.badRequest().body("Player not found!");
            }

            roulette.setPlayers(newPlayer);
            return ResponseEntity.ok("Successfully added: " + newPlayer.getName() + " to roulette!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/pockets/set")
    public ResponseEntity<?> setSelectedPockets() {
        try {
            int pockets[] = {};
            roulette.setSelectedPockets(pockets);
            // System.out.println(pockets);
            return ResponseEntity.ok("Selected pockets updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // set bet amount
    @PostMapping("/bet/amount/set/{amount}")
    public ResponseEntity<?> setBetAmount(@PathVariable int amount) {
        try {
            roulette.setBetAmount(amount);
            return ResponseEntity.ok("Bet set to: " + amount);
        } catch (IllegalBetValueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // set winning pocket
    @PostMapping("/pocket/winning/set")
    public ResponseEntity<?> setWinningPocket(@RequestParam int amount) {
        try {
            roulette.setWinningPocket(amount);
            return ResponseEntity.ok("Winning pocket set to: " + amount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * =======================================================================
     * GETTERS
     * =======================================================================
     */

    // get name
    @GetMapping("/name/get")
    public ResponseEntity<?> getName() {
        if (roulette == null) {
            return ResponseEntity.badRequest().body("Roulette not found.");
        }
        return ResponseEntity.ok(roulette.getName());
    }

    // get players
    @GetMapping("/players")
    public ResponseEntity<?> getPlayer() {
        if (roulette == null) {
            return ResponseEntity.badRequest().body("Roulette not found.");
        }
        return ResponseEntity.ok(roulette.getPlayers());
    }

    // get players' count
    @GetMapping("/players/count")
    public ResponseEntity<?> getPlayerCount() {
        if (roulette == null) {
            return ResponseEntity.badRequest().body("Roulette not found.");
        }
        return ResponseEntity.ok(roulette.getPlayerCount());
    }

    // get player money
    // @GetMapping("/player/{name}/money/get")
    // public ResponseEntity<?> getPlayerMoney(@PathVariable String name) {
    //     try {
    //         MultiValueMap<String,String> map = new LinkedMultiValueMap<String, String>();
    //         // map.add("Login", name);
    //         // map.add("ColumnName", "MoneyAmount");
    //         ResponseEntity<String> responseEntity = callDbApi("http://localhost:8081/api/users/checkResources/" +name +"/MoneyAmount", map, StringType);
    //         return ResponseEntity.ok(responseEntity.getBody());
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @GetMapping("/player/{name}/getPayout")
    public ResponseEntity<?> getPlayerPayout(@PathVariable String name) {
        int ret = -1;
        Integer money = null;
        Integer oldMoney = null;
        Player pl1;
        try {
            // String ms = "   DEBUG: Players amount: " + String.valueOf(roulette.getPlayerCount());
            // String ms2 = ", Bet amount: " + String.valueOf(roulette.getBetAmount());
            // String ms3 = ", Bet: " + roulette.getBet().name();
            // return ResponseEntity.badRequest().body(ms + ms2 + ms3);
            pl1 = roulette.getPlayers().getFirst();
            oldMoney = pl1.getMoney();
            ret = roulette.getOutcome();
            if (ret < 0 ) {
                throw new IllegalArgumentException("Roulette outcome is negative");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Couldn't get outcome" + e.getMessage());
        }
        try {
            pl1 = roulette.getPlayers().getFirst();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("Login", name);
            map.add("Money", String.valueOf(pl1.getMoney()));

            ResponseEntity<Integer> responseEntity = this.callDbApi(
                "http://localhost:8081/api/users/setMoney",
                map,
                integerType
            );
            if (pl1.getMoney() != oldMoney + ret) {
                throw new IllegalStateException("Player money: " + pl1.getMoney() + " not equal ret:" + String.valueOf(ret) + " + oldMoney:" + oldMoney);
            }
            money = responseEntity.getBody();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Couldn't get payout" + e.getMessage());
        }
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/bet/ratio/get")
    public ResponseEntity<?> getBetRatio(@RequestParam String betName) {
        try {
            Bets bet = Roulette.getBet(betName.toUpperCase());
            return ResponseEntity.ok(bet.getRatio());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid bet name: " + betName);
        }
    }

    @GetMapping("/pockets/get")
    public ResponseEntity<int[]> getSelectedPockets() {
        try {
            return ResponseEntity.ok(roulette.getSelectedPockets());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // get outcome
    @GetMapping("/outcome/get")
    public ResponseEntity<?> getOutcome() {
        if (roulette == null) {
            return ResponseEntity.badRequest().body("Roulette not found.");
        }
        return ResponseEntity.ok(roulette.getOutcome());
    }

    // get payout
    @GetMapping("/payout/get")
    public ResponseEntity<?> getPayout() {
        try {
            return ResponseEntity.ok(roulette.getPayout());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get pocket color
    @GetMapping("/pocket/color/getColor")
    public ResponseEntity<?> getPocketColor() {
        try {
            return ResponseEntity.ok(Roulette.getPocketColor(roulette.getWinningPocket()));
        } catch (IllegalPocketNumberException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get winning pocket
    @GetMapping("/pocket/winning/get")
    public ResponseEntity<?> getWinningPocket() {
        try {
            return ResponseEntity.ok(roulette.getWinningPocket());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get if won
    @GetMapping("/ifWon")
    public ResponseEntity<?> getIfWon() {
        try {
            return ResponseEntity.ok(roulette.getIfWon());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PRIVATE

    private Player getPlayerFromPlayerController(String name) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(playerBaseUrl + "/" + name))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Convert response to Player object
                ObjectMapper objectMapper = new ObjectMapper();
                Player player = objectMapper.readValue(response.body(), Player.class);
                return player;
            } else {
                System.out.println("Error: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Player findPlayerByName(List<Player> players, String name) {
        Optional<Player> playerOptional = players.stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();

        return playerOptional.orElse(null); // returns null if player is not found
    }

    /*
     * ifAdjacent //no need to add
     * ifMeetAtCorner //no need to add
     * ifFormRow //no need to add
     * ifFormTwoRows //no need to add
     */
}