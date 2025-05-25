package pap2024z.z11.Gaming_institution.gamelogic;

public class Card {
    private final String suit;
    private final String rank;
    private final int rankNumber;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.rankNumber = CardData.getRankNumber(rank);
    }

    public String getSuit() {
        return this.suit;
    }
    public String getRank() {
        return this.rank;
    }

    public int getRankNumber() {
        return this.rankNumber;
    }

    public boolean equals(Card other) {
        return this.getSuit().equals(other.getSuit())
                && this.getRank().equals(other.getRank());
    }
}