package view.zen6;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import domain.Card;
import domain.CardUtils;

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
        for(var card: cards){
            card.draw(graphics);
        }
    }

    public void addCard(Card card){
        cards.add(new UICard(card, 0, 0, 100, 150, zDepth + 1, false));
    }

    public void removeCard(Card card){
        for(var c: cards){
            if(c.getCard().equals(card)){
                cards.remove(c);
                break;
            }
        }
    }

    public void removeCards(List<Card> removedCards){
        for(var card: removedCards){
            removeCard(card);
        }
    }

    public void recomputeCardsCoordinates(){
        for(int i = 0; i < cards.size(); i++){
            UICard card = cards.get(i);
            int x = this.x + 10 + i * 110;
            int y = this.y + 10;
            cards.set(i, new UICard(card.getCard(), x, y, 100, 150, zDepth + 1, false));
        }
    }

    public void selectCards(List<Card> changedCards, boolean isSelected) {
        for(var card: changedCards){
            boolean found = false;
            for(int i = 0; i < cards.size(); i++){
                UICard uiCard = cards.get(i);
                if(uiCard.getCard().equals(card)){
                    found = true;
                    cards.set(i, new UICard(uiCard.getCard(), uiCard.x(), uiCard.y(), uiCard.width(), uiCard.height(), uiCard.zDepth(), isSelected));
                }
            }
            if(!found){
                IO.println("Exception: Current hand does not correspond with requested card: " + card);
            }
        }
    }

    public void removeAllCards(){
        cards.clear();
    }

    public java.util.List<UICard> getCards() {
        return new java.util.ArrayList<>(cards);
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
