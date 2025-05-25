package pap2024z.z11.Gaming_institution.gamelogic;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public abstract class CardContainer {
    protected List<Card> cards;

    public CardContainer() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() { return this.cards; }

    public int getCardsLeft() { return this.cards.size(); }

    public boolean getIfEmpty() { return this.cards.isEmpty(); }

    public void setCards(CardContainer other) { this.cards = new ArrayList<>(other.getCards()); }

    public void setCards(List<Card> cards) { this.cards = new ArrayList<>(cards); }

    public void setEmpty() { this.cards = new ArrayList<>(); }

    public boolean equals(CardContainer other) {
        if (this.getCardsLeft() != other.getCardsLeft())
            return false;
        for (int i = 0; i < this.getCardsLeft(); i++) {
            if (!this.getCards().get(i).equals(other.getCards().get(i)))
                return false;
        }
        return true;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void sortByRank() {
        this.cards.sort((c1, c2) -> {
            int cardRank1 = c1.getRankNumber();
            int cardRank2 = c2.getRankNumber();
            return Integer.compare(cardRank1, cardRank2);
        });
    }

    public void sortBySuit() {
        this.cards.sort(Comparator.comparingInt((Card c) -> CardData.legalSuits.indexOf(c.getSuit())).thenComparingInt(Card::getRankNumber));
    }
}
