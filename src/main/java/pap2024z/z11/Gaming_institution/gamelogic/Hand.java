package pap2024z.z11.Gaming_institution.gamelogic;

import pap2024z.z11.Gaming_institution.exceptions.InvalidCardException;
import pap2024z.z11.Gaming_institution.exceptions.PlayFromEmptyException;

public class Hand extends CardContainer {

    public Hand() {
        super();
    }

    public Card playCard(Card card) throws PlayFromEmptyException, InvalidCardException {
        if (this.getIfEmpty())
            throw new PlayFromEmptyException("Cannot play from an empty hand");
        else if (!this.cards.contains(card))
            throw new InvalidCardException("This player hasn't got card like this");
        else {
            return this.cards.remove(this.cards.indexOf(card));
        }
    }

    public Card playCard(int index) throws PlayFromEmptyException, IndexOutOfBoundsException {
        if (this.getIfEmpty()) {
            throw new PlayFromEmptyException("Cannot play from an empty hand");
        }
        else if (index < 0 || index > this.getCardsLeft() - 1) {
            throw new IndexOutOfBoundsException("This player doesn't have a card with such index");
        }
        else {
            return this.cards.remove(index);
        }
    }
}
