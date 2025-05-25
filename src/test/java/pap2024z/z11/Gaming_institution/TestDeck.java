package pap2024z.z11.Gaming_institution;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pap2024z.z11.Gaming_institution.exceptions.DrawFromEmptyException;
import pap2024z.z11.Gaming_institution.gamelogic.Card;
import pap2024z.z11.Gaming_institution.gamelogic.CardData;
import pap2024z.z11.Gaming_institution.gamelogic.Deck;

public class TestDeck {

    private Deck deck1, deck2;

    @BeforeEach
    public void setUp() {
        try {
            deck1 = new Deck();
            deck2 = new Deck();
        } catch (Exception e) {
            fail("Exception shouldn't be thrown when deck is created correctly" + e.getMessage());
        }

        // mocked random to get predictable results
        // using mockRandom in deck.shuffle() method should
        // return reversed deck as a result
        Random mockRandom = new Random() {
            private final int[] predefined = {
                    51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36,
                    35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20,
                    19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0
            };
            private int index = 0;

            @Override
            public int nextInt(int bound) {
                return predefined[index++ % predefined.length];
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        CardData cardData = new CardData();
        ArrayList<Card> standardCards = new ArrayList<>();
        for (String suit : cardData.getLegalSuits()) {
            for (String rank : cardData.getLegalRanks()) {
                try {
                    standardCards.add(new Card(suit, rank));
                } catch (Exception e) {
                    fail("Exception shouldn't be thrown when deck is created correctly" + e.getMessage());
                }
            }
        }

        for (int i = 0; i < 52; i++) {
            Card c1 = deck1.getCards().get(i);
            Card c2 = standardCards.get(i);
            assertTrue(c1.equals(c2));
        }
    }

    @Test
    public void testFull() {
        assertEquals(52, deck1.getCardsLeft());
        assertTrue(deck1.isFull());
        assertFalse(deck1.getIfEmpty());
    }

    @Test
    public void testEmpty() {
        deck1.setEmpty();
        assertEquals(0, deck1.getCardsLeft());
        assertFalse(deck1.isFull());
        assertTrue(deck1.getIfEmpty());
    }

    @Test
    public void testShuffle() {

        // in normal case you call this method by:
        // deck.shuffle(new Random());
        deck1.shuffleDeck();

        assertEquals(52, deck1.getCardsLeft());
        assertFalse(deck1.getIfEmpty());
        assertTrue(deck1.isFull());

        assertFalse(deck1.equals(deck2));
    }

    @Test
    public void dealFromFull() {
        try {
            Card card1 = new Card("hearts", "2");
            Card card2 = deck1.dealCard();
            assertTrue(card1.equals(card2));

            assertEquals(51, deck1.getCardsLeft());
            assertFalse(deck1.isFull());
            assertFalse(deck1.getIfEmpty());

        } catch (DrawFromEmptyException e) {
            fail("Exception shouldn't be thrown when card's rank is correct\n" + e.getMessage());
        }
    }

    @Test
    public void testReset() {
        deck1.shuffleDeck();
        assertFalse(deck1.equals(deck2));
        try {
            deck1.resetDeck();
        } catch (Exception e) {
            fail("Exception shouldn't be thrown when reset deck is working correctly.\n" + e.getMessage());
        }
        assertTrue(deck1.equals(deck2));
    }

    @Test
    public void dealFromEmpty() {
        deck1.setEmpty();
        DrawFromEmptyException e = assertThrows(
          DrawFromEmptyException.class, () -> { deck1.dealCard(); }
        );
    }

    @Test
    public void testEquals() {
        assertTrue(deck1.equals(deck2));
        assertTrue(deck2.equals(deck1));
    }

    @Test
    public void testEqualsDifferentSize() {
        try {
            deck1.dealCard();
        }
        catch (DrawFromEmptyException e) {
            System.out.println(e.getMessage());
        }
        assertFalse(deck1.equals(deck2));
    }

    @Test
    public void testEqualsDifferentCards() {
        deck1.shuffleDeck();
        assertFalse(deck1.equals(deck2));
    }
}
