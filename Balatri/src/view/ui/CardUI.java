package view.ui;

import domain.Card;

public class CardUI {

    private final Card card;
    private final int x;
    private final int y;
    //private final int width  = 80;
    //private final int height = 100;

    public CardUI(Card card, int x, int y) {
        this.card    = card;
        this.x       = x;
        this.y       = y;
    }

    public Card getCard() { return card; }
}