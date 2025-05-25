package pap2024z.z11.Gaming_institution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import pap2024z.z11.Gaming_institution.gamelogic.CardData;

public class TestCardData {

    @Test
    public void testConstructor() {
        CardData cardData = new CardData();
        List<String> legalSuits = new ArrayList<>(
                Arrays.asList("hearts", "diamonds", "clubs", "spades")
        );
        List<String> legalRanks = new ArrayList<>(
                Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
        );

        assertArrayEquals( cardData.getLegalSuits().toArray(new String[0]),
                legalSuits.toArray(new String[0]) );

        assertArrayEquals( cardData.getLegalRanks().toArray(new String[0]),
                legalRanks.toArray(new String[0]));

        assertEquals(2, CardData.getRankNumber("2"));
        assertEquals(14, CardData.getRankNumber("A"));
    }
}
