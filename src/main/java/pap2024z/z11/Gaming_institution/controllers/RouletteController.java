// package pap2024z.z11.Gaming_institution.controllers;

// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpResponse;
// import java.util.List;
// import java.util.Optional;
// import java.net.http.HttpRequest;

// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import pap2024z.z11.Gaming_institution.exceptions.EmptyNameException;
// import pap2024z.z11.Gaming_institution.exceptions.IllegalBetValueException;
// import pap2024z.z11.Gaming_institution.exceptions.IllegalMoneyValueException;
// //import pap2024z.z11.Gaming_institution.exceptions.IllegalMoneyValueException;
// import pap2024z.z11.Gaming_institution.exceptions.IllegalPocketNumberException;
// import pap2024z.z11.Gaming_institution.game.Roulette;
// import pap2024z.z11.Gaming_institution.game.Roulette.Bets;
// import pap2024z.z11.Gaming_institution.gamelogic.Player;
// import org.springframework.web.bind.annotation.RequestParam;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/roulette")
// public class RouletteController {

//     private Roulette roulette = new Roulette();
//     private final String playerBaseUrl = "http://localhost:8081/api/player";

//     private final ParameterizedTypeReference<Integer> integerType = new ParameterizedTypeReference<Integer>() {};
//     private final ParameterizedTypeReference<List<String>> listStringType = new ParameterizedTypeReference<List<String>>() {};
//     private final ParameterizedTypeReference<String> StringType = new ParameterizedTypeReference<String>() {};
//     private final RestTemplate restTemplate;

//     public RouletteController(RestTemplateBuilder builder) {
//         this.restTemplate = builder.build();
//     }

//     private <T> ResponseEntity<T> callDbApi(String url, MultiValueMap<String,String> paramMap, ParameterizedTypeReference<T> returnType) throws Exception {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//         HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(paramMap, headers);

//         ResponseEntity<T> responseEntity = restTemplate.exchange(
//                 url,
//                 HttpMethod.POST,
//                 requestEntity,
//                 returnType
//         );

//         return responseEntity;
//     }

//     @PostMapping("/reset")
//     public ResponseEntity<String> resetRoulette() {
//         this.roulette = new Roulette();
//         return ResponseEntity.ok("Roulette created/reset successfully.");
//     }

//     // spin
//     @PostMapping("/spin")
//     public ResponseEntity<String> spinRoulette() {
//         roulette.spin();
//         return ResponseEntity.ok(Integer.toString(roulette.getWinningPocket()));
//     }

//     // place bet
//     @PostMapping("/bet/place/{betName}")
//     public ResponseEntity<?> roulettePlaceBet(@PathVariable String betName) {
//         try {
//             Bets bet = Roulette.getBet(betName.toUpperCase());
//             roulette.placeBet(bet, roulette.getSelectedPockets(), roulette.getBetAmount());
//             return ResponseEntity.ok("Bet placed successfully!");
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     /*
//      * =======================================================================
//      * SETTERS
//      * =======================================================================
//      */

//     // set name
//     @PostMapping("/name/set/{name}")
//     public ResponseEntity<?> setRouletteName(@PathVariable String name) {
//         try {
//             roulette.setName(name);
//             return ResponseEntity.ok("Roulette name set to: " + name);
//         } catch (EmptyNameException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // set money
//     @PostMapping("/player/{name}/money/set")
//     public ResponseEntity<?> setPlayerMoney(@PathVariable String name, @RequestParam int amount) {
//         try {
//             if (name == null || name.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Name is required!");
//             }

//             Player player = findPlayerByName(roulette.getPlayers(), name);
//             if (player == null) {
//                 return ResponseEntity.badRequest().body("Player not found!");
//             }
//             roulette.setPlayerMoney(player, amount);
//             return ResponseEntity.ok(player.getName() + "'s balance: " + amount);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // add money
//     @PostMapping("/player/{name}/money/add")
//     public ResponseEntity<?> addPlayerMoney(@PathVariable String name, @RequestParam int amount) {
//         try {
//             if (name == null || name.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Name is required!");
//             }

//             Player player = findPlayerByName(roulette.getPlayers(), name);
//             if (player == null) {
//                 return ResponseEntity.badRequest().body("Player not found!");
//             }
//             roulette.addPlayerMoney(player, amount);
//             return ResponseEntity.ok("Successfully added: " + amount + " to " + player.getName() + "'s account!");
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/add/player/{name}")
//     public ResponseEntity<?> addPlayerToRoulette(@PathVariable String name) {
//         try {
//             if (name == null || name.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Name is required!");
//             }

//             Player newPlayer = getPlayerFromPlayerController(name);

//             boolean playerExists = roulette.getPlayers().stream()
//                     .anyMatch(player -> player.getName().equals(newPlayer.getName()));
//             if (playerExists) {
//                 return ResponseEntity.badRequest().body("Player already exists in the roulette!");
//             }

//             if (newPlayer == null) {
//                 return ResponseEntity.badRequest().body("Player not found!");
//             }

//             roulette.addPlayer(newPlayer);
//             return ResponseEntity.ok("Successfully added: " + newPlayer.getName() + " to roulette!");
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }
    
//     @PostMapping("/set/player/{name}")
//     public ResponseEntity<?> setPlayerToRoulette(@PathVariable String name) {
//         try {
//             if (name == null || name.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Name is required!");
//             }

//             Player newPlayer = getPlayerFromPlayerController(name);

//             boolean playerExists = roulette.getPlayers().stream()
//                     .anyMatch(player -> player.getName().equals(newPlayer.getName()));
//             if (playerExists) {
//                 return ResponseEntity.badRequest().body("Player already exists in the roulette!");
//             }

//             if (newPlayer == null) {
//                 return ResponseEntity.badRequest().body("Player not found!");
//             }

//             roulette.setPlayers(newPlayer);
//             return ResponseEntity.ok("Successfully added: " + newPlayer.getName() + " to roulette!");
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/pockets/set")
//     public ResponseEntity<?> setSelectedPockets() {
//         try {
//             int pockets[] = {};
//             roulette.setSelectedPockets(pockets);
//             // System.out.println(pockets);
//             return ResponseEntity.ok("Selected pockets updated successfully!");
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // set bet amount
//     @PostMapping("/bet/amount/set/{amount}")
//     public ResponseEntity<?> setBetAmount(@PathVariable int amount) {
//         try {
//             roulette.setBetAmount(amount);
//             return ResponseEntity.ok("Bet set to: " + amount);
//         } catch (IllegalBetValueException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // set winning pocket
//     @PostMapping("/pocket/winning/set")
//     public ResponseEntity<?> setWinningPocket(@RequestParam int amount) {
//         try {
//             roulette.setWinningPocket(amount);
//             return ResponseEntity.ok("Winning pocket set to: " + amount);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     /*
//      * =======================================================================
//      * GETTERS
//      * =======================================================================
//      */

//     // get name
//     @GetMapping("/name/get")
//     public ResponseEntity<?> getName() {
//         if (roulette == null) {
//             return ResponseEntity.badRequest().body("Roulette not found.");
//         }
//         return ResponseEntity.ok(roulette.getName());
//     }

//     // get players
//     @GetMapping("/players")
//     public ResponseEntity<?> getPlayer() {
//         if (roulette == null) {
//             return ResponseEntity.badRequest().body("Roulette not found.");
//         }
//         return ResponseEntity.ok(roulette.getPlayers());
//     }

//     // get players' count
//     @GetMapping("/players/count")
//     public ResponseEntity<?> getPlayerCount() {
//         if (roulette == null) {
//             return ResponseEntity.badRequest().body("Roulette not found.");
//         }
//         return ResponseEntity.ok(roulette.getPlayerCount());
//     }

//     // get player money
//     @GetMapping("/player/{name}/money/get")
//     public ResponseEntity<?> getPlayerMoney(@PathVariable String name) {
//         try {
//             if (name == null || name.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Name is required!");
//             }

//             Player player = findPlayerByName(roulette.getPlayers(), name);
//             if (player == null) {
//                 return ResponseEntity.badRequest().body("Player not found!");
//             }
//             return ResponseEntity.ok(roulette.getPlayerMoney(player));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @GetMapping("/player/{name}/getPayout")
//     public ResponseEntity<?> getPlayerPayout(@PathVariable String name) {
//         int ret;
//         int money;
//         try {
//             ret = roulette.getOutcome();
//             MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//             map.add("Login", name);
//             map.add("MoneyAmount", String.valueOf(ret));

//             ResponseEntity<Integer> responseEntity = this.callDbApi(
//                 "http://localhost:8081/api/users/setMoney",
//                 map,
//                 integerType
//             );

//             money = responseEntity.getBody();
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Couldn't get payout" + e.getMessage());
//         }
//         return ResponseEntity.ok(money);
//     }

//     @GetMapping("/bet/ratio/get")
//     public ResponseEntity<?> getBetRatio(@RequestParam String betName) {
//         try {
//             Bets bet = Roulette.getBet(betName.toUpperCase());
//             return ResponseEntity.ok(bet.getRatio());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Invalid bet name: " + betName);
//         }
//     }

//     @GetMapping("/pockets/get")
//     public ResponseEntity<int[]> getSelectedPockets() {
//         try {
//             return ResponseEntity.ok(roulette.getSelectedPockets());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(null);
//         }
//     }

//     // get outcome
//     @GetMapping("/outcome/get")
//     public ResponseEntity<?> getOutcome() {
//         if (roulette == null) {
//             return ResponseEntity.badRequest().body("Roulette not found.");
//         }
//         return ResponseEntity.ok(roulette.getOutcome());
//     }

//     // get payout
//     @GetMapping("/payout/get")
//     public ResponseEntity<?> getPayout() {
//         try {
//             return ResponseEntity.ok(roulette.getPayout());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // get pocket color
//     @GetMapping("/pocket/color/getColor")
//     public ResponseEntity<?> getPocketColor() {
//         try {
//             return ResponseEntity.ok(Roulette.getPocketColor(roulette.getWinningPocket()));
//         } catch (IllegalPocketNumberException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // get winning pocket
//     @GetMapping("/pocket/winning/get")
//     public ResponseEntity<?> getWinningPocket() {
//         try {
//             return ResponseEntity.ok(roulette.getWinningPocket());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // get if won
//     @GetMapping("/ifWon")
//     public ResponseEntity<?> getIfWon() {
//         try {
//             return ResponseEntity.ok(roulette.getIfWon());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     // PRIVATE

//     private Player getPlayerFromPlayerController(String name) {
//         try {
//             HttpClient client = HttpClient.newHttpClient();
//             HttpRequest request = HttpRequest.newBuilder()
//                     .uri(URI.create(playerBaseUrl + "/" + name))
//                     .build();

//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             if (response.statusCode() == 200) {
//                 // Convert response to Player object
//                 ObjectMapper objectMapper = new ObjectMapper();
//                 Player player = objectMapper.readValue(response.body(), Player.class);
//                 return player;
//             } else {
//                 System.out.println("Error: " + response.statusCode() + " " + response.body());
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return null;
//     }

//     private Player findPlayerByName(List<Player> players, String name) {
//         Optional<Player> playerOptional = players.stream()
//                 .filter(player -> player.getName().equals(name))
//                 .findFirst();

//         return playerOptional.orElse(null); // returns null if player is not found
//     }

//     /*
//      * ifAdjacent //no need to add
//      * ifMeetAtCorner //no need to add
//      * ifFormRow //no need to add
//      * ifFormTwoRows //no need to add
//      */
// }