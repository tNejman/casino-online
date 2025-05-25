package pap2024z.z11.Gaming_institution.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pap2024z.z11.Gaming_institution.exceptions.EmptyNameException;
import pap2024z.z11.Gaming_institution.exceptions.UnknownPlayerException;
import pap2024z.z11.Gaming_institution.exceptions.FullPlayerListException;
import pap2024z.z11.Gaming_institution.gamelogic.Player;

public abstract class Game {
    String name;
    List<Player> players;
    // I think we should use queue structure for player order
    // Or maybe we can just use order in players list
    // Player[] playerOrder = new Player[0]; // possibly redundant member
    int turnNumber = 0;
    Player currentPlayer;

    // no constructor

    public final String getName() {
        return this.name;
    }

    public final List<Player> getPlayers() {
        return this.players;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getPlayerMoney(Player player) throws UnknownPlayerException {
        if (!players.contains(player)) {
            throw new UnknownPlayerException("Cannot check money of player that is not in this game");
        } else {
            return player.getMoney();
        }
    }

    public void setName(String name) throws EmptyNameException {
        if (name.isEmpty())
            throw new EmptyNameException("Name cannot be empty!");
        this.name = name;
    }

    public void setPlayers(List<Player> players) throws FullPlayerListException {
        Collections.copy(this.players, players);
    }

    public void addPlayer(Player player) throws FullPlayerListException {
        if (this.players != null) {
            this.players.add(player);
        } else {
            this.players = new ArrayList<>();
            this.players.add(player);
        }
        if (this.players.getFirst().equals(player)) {
            currentPlayer = player;
        }
        currentPlayer.joinGame("roulette");
    }

    public void setPlayerMoney(Player player, int money) {
        if (!players.getFirst().equals(player)) {
            throw new UnknownPlayerException("Cannot set money of player that is not in this game");
        } else {
            player.setMoney(money);
        }
    }

    public void addPlayerMoney(Player player, int money) {
        if (!players.contains(player)) {
            throw new UnknownPlayerException("Cannot modify money of player that is not in this game");
        } else {
            player.addMoney(money);
        }
    }

    public void shufflePlayerOrder() {
        Collections.shuffle(players);
    }
}