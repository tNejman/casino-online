package pap2024z.z11.Gaming_institution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import pap2024z.z11.Gaming_institution.gamelogic.Card;

public class TestCard {

    @Test
    public void testConstructor() {
        // Testing constructor with arguments and getters
            Card card = new Card("hearts", "2");

            assertEquals("2", card.getRank());
            assertEquals("hearts", card.getSuit());
            assertEquals(2, card.getRankNumber());
    }

    @Test
    public void testEquals() {
        Card card1 = new Card("hearts", "4");
        Card card2 = new Card("hearts", "4");
        Card card3 = new Card("spades", "A");

        assertTrue(card1.equals(card2));
        assertFalse(card1.equals(card3));
    }
}

