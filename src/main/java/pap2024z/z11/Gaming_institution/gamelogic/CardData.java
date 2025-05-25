package pap2024z.z11.Gaming_institution.gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardData {
    // legalSuits must be public and static; required elsewhere
    public static final List<String> legalSuits = new ArrayList<>(
            Arrays.asList("hearts", "diamonds", "clubs", "spades"));
    public static final List<String> legalRanks = new ArrayList<>(
            Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"));
    private static final int[] legalRankNumbers = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    public List<String> getLegalSuits() { return legalSuits; }

    public List<String> getLegalRanks() { return legalRanks; }

    public static int getRankNumber(String rank) {
        return legalRankNumbers[legalRanks.indexOf(rank)]; }
}
