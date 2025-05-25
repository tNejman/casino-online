package pap2024z.z11.Gaming_institution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pap2024z.z11.Gaming_institution.exceptions.InvalidCardException;
import pap2024z.z11.Gaming_institution.exceptions.PlayFromEmptyException;
import pap2024z.z11.Gaming_institution.gamelogic.Card;
import pap2024z.z11.Gaming_institution.gamelogic.Hand;

public class TestHand {

    private Hand h1;
    private Card c1,c2,c3,c4,c5;
    List<Card> hold;
    Random rnd;

    @BeforeEach
    public void setUp() {
        h1 = new Hand();
        c1 = new Card("hearts", "2");
        c2 = new Card("hearts", "K");
        c3 = new Card("spades", "A");
        c4 = new Card("clubs", "7");
        c5 = new Card("diamonds", "Q");
        hold = new ArrayList<>(List.of(c1, c2, c3, c4, c5));
        rnd = new Random();
    }

    @Test
    public void testDefaultConstructor() {
        Hand h2 = new Hand();

        assertTrue(h2.getIfEmpty());
        assertEquals(0, h2.getCardsLeft());

        assertArrayEquals(new ArrayList<>().toArray(), h2.getCards().toArray());
    }

    @Test
    public void testPlayCardId() {
        // try to play card with id out of bounds
        h1.setCards(hold);
        IndexOutOfBoundsException exc = assertThrows(IndexOutOfBoundsException.class, () -> {
            h1.playCard(6);
        });

        // play card correctly
        h1.setCards(hold);

        while (!h1.getIfEmpty()) {
            int randIndex = rnd.nextInt(h1.getCardsLeft());
            try {
                h1.playCard(randIndex);
            } catch (PlayFromEmptyException | IndexOutOfBoundsException e) {
                fail();
            }
        }
    }

    @Test
    public void testPlayCardCard() {
        // play cards correctly
        h1.setCards(hold);
        List<Card> holdTemp = new ArrayList<>(hold);

        while (!h1.getIfEmpty()) {
            int randIndex = rnd.nextInt(holdTemp.size());
            Card cTemp = holdTemp.get(randIndex);
            holdTemp.remove(randIndex);

            try {
                h1.playCard(cTemp);
            } catch (PlayFromEmptyException | InvalidCardException e) {
                fail();
            }
        }

        // try to play the same card twice
        h1.setCards(hold);
        try {
            h1.playCard(c1);
        } catch (PlayFromEmptyException | InvalidCardException e) {
            fail();
        }
        InvalidCardException exc = assertThrows(InvalidCardException.class, () -> {
            h1.playCard(c1);
        });

        // try to play card that is not in hand
        h1.setCards(hold);
        Card cTemp = new Card("hearts", "2");

        InvalidCardException exc2 = assertThrows(InvalidCardException.class, () -> {
            h1.playCard(cTemp);
        });
    }

    @Test
    public void testPlayCardFromEmpty() {
        // try to play a card from an empty hand
        h1.setEmpty();

        PlayFromEmptyException exc = assertThrows(PlayFromEmptyException.class, () -> {
            h1.playCard(0);
        });
    }
}
