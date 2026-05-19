package view.zen6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domain.Card;
import model.GameState;

public final class UIHandContainer implements UIObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private int zDepth; 
    private List<UICard> cards = new ArrayList<>();

    public UIHandContainer(int x, int y, int width, int height, int zDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    @Override
    public int zDepth(){ return zDepth;}

    @Override
    public void draw(Graphics2D graphics) {
        // Draw container background
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, width, height);
        for(var card: cards){
            card.draw(graphics);
        }
    }

    private void sortCards() {
        cards.sort(Comparator
            .comparingInt((UICard c) -> c.getCard().rank().getValue())
            .thenComparing(c -> c.getCard().suit().ordinal()));
    }

    public void addCards(List<Card> changedCards) {
        changedCards.forEach(c -> addCard(c));
        recomputeCardsCoordinates();
    }

    public void addCard(Card card){
        cards.add(new UICard(card, 0, 0, 100, 150, zDepth + 1, false));
    }

    public void removeCards(List<Card> removedCards){
        removedCards.forEach(c -> removeCard(c));
        recomputeCardsCoordinates();
    }

    public void removeCard(Card card){
        for(var c: cards){
            if(c.getCard().equals(card)){
                cards.remove(c);
                break;
            }
        }
    }

    public void recomputeCardsCoordinates(){
        sortCards();
        for(int i = 0; i < cards.size(); i++){
            UICard card = cards.get(i);
            int x = this.x + 10 + i * 110;
            int y = this.y + 30;
            cards.set(i, new UICard(card.getCard(), x, y, 100, 150, zDepth + 1, card.isSelected()));
        }
    }

    public void selectCards(List<Card> changedCards, boolean isSelected) {
        for(var card: changedCards){
            boolean found = false;
            for(int i = 0; i < cards.size(); i++){
                UICard uiCard = cards.get(i);
                if(uiCard.getCard().equals(card)){
                    found = true;
                    int newY = uiCard.y() + (isSelected ? -20 : 20);
                    cards.set(i, new UICard(uiCard.getCard(), uiCard.x(), newY, uiCard.width(), uiCard.height(), uiCard.zDepth(), isSelected));
                }
            }
            if(!found){
                IO.println("Exception: Current hand does not correspond with requested card: " + card);
            }
        }
    }

    public void refreshHand(List<Card> cards) {
        removeAllCards();
        cards.forEach(c -> addCard(c));
        recomputeCardsCoordinates();
    }

    public void removeAllCards(){
        cards.clear();
    }

    public java.util.List<UICard> getCards() {
        return new ArrayList<>(cards);
    }

    public UICard getClickedCard(Point p) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            UICard c = cards.get(i);
            if (c.contains(p)) {
                return c;
            }
        }
        return null;
    }
}
