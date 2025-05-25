package pap2024z.z11.Gaming_institution.gamelogic;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import pap2024z.z11.Gaming_institution.exceptions.AlreadyInGameException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalGameTypeException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalMoneyValueException;
import pap2024z.z11.Gaming_institution.exceptions.InvalidCardException;
import pap2024z.z11.Gaming_institution.exceptions.PlayFromEmptyException;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    private int id;
    private int sessionId;
    private String name;
    private String password;
    private String email;
    private int age;
    private final Hand hand;
    private int money = 0;
    boolean ifReady = false;
    boolean ifInGame = false;
    String gameType = null;
    static final String[] legalGameTypes = { "roulette", "poker", "blackjack" };

    @JsonCreator
    public Player(@JsonProperty("name") String name) {
        this.setName(name);
        this.hand = new Hand();
    }

    @JsonCreator
    public Player(@JsonProperty("id") int id,
                  @JsonProperty("name") String name, 
                  @JsonProperty("password") String password,
                  @JsonProperty("email") String email,
                  @JsonProperty("age") int age) {
        this.setId(id);
        this.setSessionId(-1);
        this.setName(name);
        this.setPassword(password);
        this.setEmail(email);
        this.setAge(age);
        this.hand = new Hand();
    }

    public int getId() {
        return this.id;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public int getAge() {
        return this.age;
    }

    public List<Card> getHand() {
        return this.hand.getCards();
    }

    public int getHandSize() {
        return this.hand.getCardsLeft();
    }

    public int getMoney() {
        return this.money;
    }

    public boolean getIfInGame() {
        return this.ifInGame;
    }

    public boolean getIfReady() {
        return this.ifReady;
    }

    public boolean getIfHandEmpty() {
        return this.hand.getIfEmpty();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @JsonSetter("hand")
    public void setHand(List<Card> cards) {
        this.hand.setCards(cards);
    }

    public void setHand(CardContainer other) {
        this.hand.setCards(other);
    }

    public void setMoney(int money) throws IllegalMoneyValueException {
        if (money < 0) {
            throw new IllegalMoneyValueException("Player money cannot be negative");
        } else {
            this.money = money;
        }
    }

    public void setIfInGame(boolean ifInGame) {
        this.ifInGame = ifInGame;
    }

    public void setIfReady(boolean ifReady) {
        this.ifReady = ifReady;
    }

    public Card playCard(Card card) throws PlayFromEmptyException, InvalidCardException {
        return this.hand.playCard(card);
    }

    public Card playCard(int index) throws PlayFromEmptyException, IndexOutOfBoundsException {
        return this.hand.playCard(index);
    }

    public void drawCard(Card card) {
        this.hand.addCard(card);
    }

    public void sortHandBySuit() {
        this.hand.sortBySuit();
    }

    public void sortHandByRank() {
        this.hand.sortByRank();
    }

    public void joinGame(String gameType) throws IllegalGameTypeException, AlreadyInGameException {
        if (this.ifInGame) {
            throw new AlreadyInGameException("This player is already in game: '" + this.gameType + "'");
        }
        if (!Arrays.asList(legalGameTypes).contains(gameType)) {
            throw new IllegalGameTypeException("'" + gameType + "' is not a valid game type");
        } else {
            this.ifInGame = true;
            this.gameType = gameType;
        }
    }

    public void leaveGame() {
        this.ifInGame = false;
        this.ifReady = false;
        this.gameType = null;
    }

    public void addMoney(int money) {
        this.money += money;
    }
}
