package pap2024z.z11.Gaming_institution.game;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.Set;
import java.util.HashSet;

import pap2024z.z11.Gaming_institution.exceptions.FullPlayerListException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalBetValueException;
//import exceptions.IllegalGameTypeException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalMoneyValueException;
import pap2024z.z11.Gaming_institution.exceptions.IllegalPocketNumberException;
import pap2024z.z11.Gaming_institution.gamelogic.Player;

import org.springframework.stereotype.Service;

@Service
public class Roulette extends Game {

    public static final int[][] POCKETS = {
            { 0 },
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 },
            { 10, 11, 12 },
            { 13, 14, 15 },
            { 16, 17, 18 },
            { 19, 20, 21 },
            { 22, 23, 24 },
            { 25, 26, 27 },
            { 28, 29, 30 },
            { 31, 32, 33 },
            { 34, 35, 36 }
    };

    public static final int[] RED = { 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 };
    public static final int[] BLACK = { 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 };
    public static final int[] GREEN = { 0 };

    public enum Bets {
        // inside bets
        STRAIGHT_UP(35),
        SPLIT(17),
        STREET(11),
        CORNER(8),
        FIRST_FOUR(8),
        DOUBLE_STREET(5),

        // outside bets
        GREEN(13),
        RED(1),
        BLACK(1),
        ODD(1),
        EVEN(1),
        LOW(1),
        HIGH(1),
        DOZENS(2),
        COLUMNS(2);

        private final int ratio;

        Bets(int ratio) {
            this.ratio = ratio;
        }

        public int getRatio() {
            return ratio;
        }
    }

    private int[] selectedPockets = new int[0];
    private int winningPocket = 0;
    private int betAmount = -1;
    private Bets bet;

    public Roulette() {
        this.players = new ArrayList<>();
    }

    public Roulette(Player player) throws FullPlayerListException {
        this.players = new ArrayList<>();
        this.setPlayers(player);
    }

    @Override
    public void addPlayer(Player player) throws FullPlayerListException {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null");
        if (!this.players.isEmpty())
            throw new FullPlayerListException("There already is player in this game");
        this.players = new ArrayList<>();
        this.players.add(player);
        this.currentPlayer = player;
        player.joinGame("roulette");
    }

    @Override
    public void setPlayers(List<Player> players) throws FullPlayerListException {
        // There can be only 1 Player playing roulette
        this.players = new ArrayList<>();
        this.addPlayer(players.getFirst());
        this.currentPlayer = players.getFirst();
    }

    public void setPlayers(Player player) throws FullPlayerListException {
        this.players = new ArrayList<>();
        this.addPlayer(player);
        this.currentPlayer = player;
    }

    @Override
    public void shufflePlayerOrder() {
        return;
    }

    public boolean getIfWon() {
        if(selectedPockets.length==0)
        {
            throw new IllegalStateException("Selected pockets are empty");
        }  
        for (int pocket : selectedPockets) {
            if (pocket == winningPocket)
                return true;
        }
        return false;
    }

    public void setBetAmount(int amount) throws IllegalBetValueException {
        if (amount < 0) {
            throw new IllegalBetValueException("Only positive values can be bet");
        } else {
            this.betAmount = amount;
        }
    }

    public void setWinningPocket(int pocket) {
        this.winningPocket = pocket;
    }

    public void setSelectedPockets(int[] pockets) {
        this.selectedPockets = pockets;
    }

    public int getPayout() throws IllegalStateException {
        if (this.betAmount < 0) {
            throw new IllegalStateException("No bet amount specified"); 
        } else if (this.bet == null) {
            throw new IllegalStateException("No bet set");
        } else {
            int payout = this.betAmount * (getBetRatio(this.bet) + 1);
            this.betAmount = -1;
            this.bet = null;
            return payout;
        }
    }

    public static Bets getBet(String betName) {
        return Bets.valueOf(betName);
    }

    public Bets getBet() {
        return this.bet;
    }

    public static int getBetRatio(Bets bet) {
        return bet.getRatio();
    }

    public int getBetAmount() {
        return this.betAmount;
    }

    public int getWinningPocket() {
        return this.winningPocket;
    }

    public int[] getSelectedPockets() {
        return this.selectedPockets;
    }

    public int getOutcome() {
        if (this.getIfWon()) {
            int payout = this.getPayout();
            if (payout <= 0) {
                throw new IllegalStateException("Payout shan't have returned 0");
            }
            // int old_money = this.currentPlayer.getMoney();
            this.addPlayerMoney(currentPlayer, payout);
            // int new_money = this.currentPlayer.getMoney();
            return payout;
        } else
            return 0;
    }

    public void placeBet(Bets bet, int[] selectedPockets, int money)
            throws IllegalBetValueException, IllegalMoneyValueException, IllegalStateException {
        if (players.isEmpty()) {
            throw new IllegalStateException("Cannot place a bet, when no players is in game");
        }
        // if chosen a predefined bet selectedPockets must be of length 0
        if (selectedPockets == null) {
            throw new IllegalBetValueException("Cannot pass null as pockets[]");
        }
        if (money < 0) {
            throw new IllegalMoneyValueException("Cannot bet negative amount of money");
        }
        if (money > currentPlayer.getMoney()) {
            throw new IllegalMoneyValueException("This player doesn't have enough money for such a bet");
        }
        Set<Integer> set = new HashSet<>();
        for (int num : selectedPockets) {
            if (num < 0)
                throw new IllegalBetValueException("Pocket ID's must be positive");
            if (num > 36)
                throw new IllegalBetValueException("Pocket ID's cannot exceed 36");
            if (!set.add(num))
                throw new IllegalBetValueException("Duplicates found in selectedPockets");
        }
        set = null; // now eligible for garbage collection
        Arrays.sort(selectedPockets);
        switch (bet) {
            // inside bets
            case STRAIGHT_UP:
                if (selectedPockets.length != 1) {
                    throw new IllegalBetValueException("STRAIGHT UP needs 1 pocket");
                }
                break;
            case SPLIT:
                if (selectedPockets.length != 2) {
                    throw new IllegalBetValueException("SPLIT needs 2 pockets");
                }
                if (!ifAdjacent(selectedPockets[0], selectedPockets[1])) {
                    throw new IllegalBetValueException("In SPLIT pockets must be adjacent");
                }
                break;
            case STREET:
                if (selectedPockets.length != 3) {
                    throw new IllegalBetValueException("STREET needs 3 pockets");
                }
                if (!ifFormRow(selectedPockets)) {
                    throw new IllegalBetValueException("In STREET pockets must form a row");
                }
                break;
            case CORNER:
                if (selectedPockets.length != 4) {
                    throw new IllegalBetValueException("CORNER needs 4 pockets");
                }
                if (!ifMeetAtCorner(selectedPockets)) {
                    throw new IllegalBetValueException("In CORNER pockets must form a 2x2 square");
                }
                break;
            case FIRST_FOUR:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In FIRST FOUR pockets are predefined");
                }
                int[] firstFour = { 0, 1, 2, 3 };
                selectedPockets = firstFour.clone();
                break;
            case DOUBLE_STREET:
                if (selectedPockets.length != 6) {
                    throw new IllegalBetValueException("DOUBLE LINE needs 6 pockets");
                }
                if (!ifFormTwoRows(selectedPockets)) {
                    throw new IllegalBetValueException("In DOUBLE LINE pockets must form two rows");
                }
                break;

            // outside bets
            case GREEN:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In GREEN pockets are predefined");
                }
                this.selectedPockets = GREEN.clone();
                break;
            case RED:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In RED pockets are predefined");
                }
                this.selectedPockets = RED.clone();
                break;
            case BLACK:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In BLACK pockets are predefined");
                }
                this.selectedPockets = BLACK.clone();
                break;
            case ODD:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In ODD pockets are predefined");
                }
                int[] odd = IntStream.rangeClosed(0, 36)
                        .filter(i -> i % 2 != 0)
                        .toArray();
                selectedPockets = odd.clone();
                break;
            case EVEN:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In EVEN pockets are predefined");
                }
                int[] even = IntStream.rangeClosed(0, 36)
                        .filter(i -> i % 2 == 0)
                        .toArray();
                selectedPockets = even.clone();
                break;
            case LOW:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In LOW pockets are predefined");
                }
                int[] low = IntStream.rangeClosed(1, 18).toArray();
                selectedPockets = low.clone();
                break;
            case HIGH:
                if (selectedPockets.length != 0) {
                    throw new IllegalBetValueException("In HIGH pockets are predefined");
                }
                int[] high = IntStream.rangeClosed(19, 36).toArray();
                selectedPockets = high.clone();
                break;
            case DOZENS:
                if (selectedPockets.length != 12) {
                    throw new IllegalBetValueException("DOZENS needs 12 pockets");
                }
                int[] firstDozen = IntStream.rangeClosed(1, 12).toArray();
                int[] secondDozen = IntStream.rangeClosed(13, 24).toArray();
                int[] thirdDozen = IntStream.rangeClosed(25, 36).toArray();
                if (!Arrays.equals(selectedPockets, firstDozen) && !Arrays.equals(selectedPockets, secondDozen)
                        && !Arrays.equals(selectedPockets, thirdDozen)) {
                    throw new IllegalBetValueException("In DOZENS pockets must cover exactly 1st, 2nd or 3rd dozen");
                }
                break;
            case COLUMNS:
                if (selectedPockets.length != 12) {
                    throw new IllegalBetValueException("COLUMNS needs 12 pockets");
                }
                int[] firstColumn = IntStream.rangeClosed(1, 36).filter(i -> i % 3 == 1).toArray();
                int[] secondColumn = IntStream.rangeClosed(1, 36).filter(i -> i % 3 == 2).toArray();
                int[] thirdColumn = IntStream.rangeClosed(1, 36).filter(i -> i % 3 == 0).toArray();
                if (!Arrays.equals(selectedPockets, firstColumn) && !Arrays.equals(selectedPockets, secondColumn)
                        && !Arrays.equals(selectedPockets, thirdColumn)) {
                    throw new IllegalBetValueException("In COLUMNS pockets must form a column");
                }
                break;
            default:
                throw new IllegalBetValueException("Bet not placed, invalid bet value");
        }
        this.addPlayerMoney(currentPlayer, -money);
        this.bet = bet;
        // this.betAmount = money;
        // this.selectedPockets = selectedPockets;
    }

    public void spin() {
        Random rnd = new Random();
        this.winningPocket = rnd.nextInt(37);
    }

    public static String getPocketColor(int number) throws IllegalPocketNumberException {
        if (number == 0)
            return "Green";
        // Red/Black pattern (European)
        for (int r : RED) {
            if (r == number) {
                return "Red";
            }
        }
        for (int b : BLACK) {
            if (b == number) {
                return "Black";
            }
        }
        throw new IllegalPocketNumberException("Pocket must be in range [0, 36] (is not)");
    }

    //
    //
    // Methods below, which check features of pockets,
    // always assume pockets given are in ascending order
    //
    //

    public static boolean ifAdjacent(int a, int b) {
        // check for adjacency to 0
        if (a == 0 && (b == 1 || b == 2 || b == 3)) {
            return true;
        }
        // adjacency to the right and below for in first or second column
        if ((a % 3 != 0) && (a + 3 == b || a + 1 == b)) {
            return true;
        }
        // adjacency below in third column
        return (a + 3 == b);
    }

    public static boolean ifMeetAtCorner(int[] arr) {
        // check if given 4 pockets form a square
        if (arr[0] == 0)
            return false;
        if (arr[0] > 33)
            return false;
        return arr[0] + 1 == arr[1] && arr[0] + 3 == arr[2] && arr[0] + 4 == arr[3];
    }

    public static boolean ifFormRow(int[] arr) {
        // check if given 3 pockets create a row
        for (int[] row : POCKETS) {
            if (Arrays.equals(arr, row))
                return true;
        }
        return false;
    }

    public static boolean ifFormTwoRows(int[] arr) {
        // check if given 6 pockets create 2 rows
        // first 3 are the first row, last 3 are the second row
        int[] row1 = new int[3];
        int[] row2 = new int[3];
        System.arraycopy(arr, 0, row1, 0, 3);
        System.arraycopy(arr, 3, row2, 0, 3);
        return ifFormRow(row1) && ifFormRow(row2) && row1 != row2;
    }
}
