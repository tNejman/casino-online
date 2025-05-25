package pap2024z.z11.Gaming_institution.game;

import pap2024z.z11.Gaming_institution.gamelogic.Card;
import pap2024z.z11.Gaming_institution.gamelogic.Deck;
import pap2024z.z11.Gaming_institution.gamelogic.Player;

public class BlackJack extends CardGame {
    // ACE can be either 1 or 10. Returns 99 to be translated later
    public enum BJCardValues {
        TWO  ("2", 2),
        THREE("3", 3),
        FOUR ("4", 4),
        FIVE ("5", 5),
        SIX  ("6", 6),
        SEVEN("7", 7),
        EIGHT("8", 8),
        NINE ("9", 9),
        TEN  ("10", 10),
        JACK ("J", 10),
        QUEEN("Q", 10),
        KING ("K", 10),
        ACE  ("A", 99); // ACE value to be translated later

        private final String rank;
        private final int value;

        // Enum constructor
        BJCardValues(String rank, int value) {
            this.rank = rank;
            this.value = value;
        }

        public static int getValue(String rank) throws IllegalArgumentException {
            for (BJCardValues cardValue : BJCardValues.values()) {
                if (cardValue.rank.equals(rank)) {
                    return cardValue.value;
                }
            }
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
    }

    private Player dealer;

    public BlackJack() {
        super();
        this.dealer = new Player("dealer");
    }

    public BlackJack(Deck deck) {
        super(deck);
    }


    public int getBJValue(Card card) throws IllegalArgumentException {
        String rank = card.getRank();
        return BJCardValues.getValue(rank);
    }

    public void hit(Player p) {
        try {
            Card c = this.deck.dealCard();
            p.drawCard(c);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stand(Player p) {
        // kod
        p.setIfReady(true);
    }

    public void dealerMove() {
        if (getHandValue(dealer) < 17) {
            hit(dealer);
        }
        else {
            stand(dealer);
        }
    }

    public int getHandValue(Player p) {
        int handValue = 0;
        p.sortHandByRank();
        for (Card c : p.getHand()) {
            int cardValue = getBJValue(c);
            if (cardValue != 99) {
                handValue += cardValue;
            }
            else if (handValue + 11 <= 21) {
                    handValue += 11;
                }
            else {
                handValue += 1;
            }
        }
        return handValue;
    }
}