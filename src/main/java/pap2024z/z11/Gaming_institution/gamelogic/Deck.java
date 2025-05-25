package pap2024z.z11.Gaming_institution.gamelogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pap2024z.z11.Gaming_institution.exceptions.DrawFromEmptyException;

// OPTIMIZATION IDEA:
// Create hand class
// Deck can inherit from it, they have very similar
// operations like, dealCard, getSize, getIfEmpty, etc.

// optimization done

public class Deck extends CardContainer {

    public Deck() {
        super();
        this.resetDeck();
    }

    public boolean isFull() {
        return this.getCardsLeft() == 52;
    }

    public void resetDeck() {
        this.cards = new ArrayList<>();
        CardData cardData = new CardData();
        for (String suit : cardData.getLegalSuits()) {
            for (String rank : cardData.getLegalRanks()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffleDeck() {
        Random random = new Random();
        Collections.shuffle(cards, random);
    }

    public Card dealCard() throws DrawFromEmptyException {
        if (this.getIfEmpty()) {
            throw new DrawFromEmptyException("Cannot deal from an empty deck");
        }
        else {
            return cards.removeFirst();
        }
    }
}
