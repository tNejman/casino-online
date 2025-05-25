package pap2024z.z11.Gaming_institution.game;

import pap2024z.z11.Gaming_institution.gamelogic.Deck;

abstract class CardGame extends Game {
    Deck deck;

    public CardGame() {
        this.setDeck();
    }

    public CardGame(Deck deck)  {
        this.deck = deck;
        this.deck.resetDeck();
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Deck deck) {
        // deck needs to be checked
        this.deck = deck;
    }

    public void setDeck() {
        this.deck = new Deck();
    }

    public void shuffleDeck() {
        this.deck.shuffleDeck();
    }
}