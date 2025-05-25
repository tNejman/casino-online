package pap2024z.z11.Gaming_institution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pap2024z.z11.Gaming_institution.exceptions.InvalidCardException;
import pap2024z.z11.Gaming_institution.exceptions.PlayFromEmptyException;
import pap2024z.z11.Gaming_institution.gamelogic.Card;
import pap2024z.z11.Gaming_institution.gamelogic.Player;

public class TestPlayer {

    private Player player1;
    private Card card1, card2;

    @BeforeEach
    public void setUp() {
        player1 = new Player("player1");
        card1 = new Card("diamonds", "10");
        card2 = new Card("spades", "8");
    }

    @Test
    public void testConstructor() {
        assertEquals("player1", player1.getName(), "Player1's name should be \"player1\"");
        assertEquals(0, player1.getHandSize(), "Player1 should be holding 0 cards");
        assertTrue(player1.getIfHandEmpty(), "Player1's hand should be empty");

        // Assert : Check that new player is not in game
        //assertFalse(player1.getIfInGame(), "Player1 should not be in game");
    }

    @Test
    public void testDrawCard() {
        player1.drawCard(card1);
        assertEquals(1, player1.getHandSize(), "Player1 should be holding 1 card");
        assertFalse(player1.getIfHandEmpty(), "Player1's hand shouldn't be empty");
    }

    @Test
    public void testPlayCard() {
        player1.drawCard(card1);
        Card playedCard;
        try {
            playedCard = player1.playCard(card1);
            assertTrue(playedCard.equals(card1));
        } catch (Exception e) {
            fail("Card drawing failed\n" + e.getMessage());
        }
        assertTrue(player1.getIfHandEmpty(), "Player1's hand should be empty");
    }

    @Test
    public void testDrawCardFromEmpty() {
        assertThrows(PlayFromEmptyException.class, () -> {
            player1.playCard(card1);
        } );
    }

    @Test
    public void testDrawInvalidCard() {
        player1.drawCard(card1);
        player1.drawCard(new Card("clubs", "A"));
        try {
            player1.playCard(card1);
        } catch (PlayFromEmptyException | InvalidCardException e) {
            fail();
        }
        assertThrows(InvalidCardException.class, () -> {
            player1.playCard(card2);
        } );
    }
}
