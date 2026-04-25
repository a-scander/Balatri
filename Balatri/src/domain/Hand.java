package domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;
    private int maxSize; 

    public Hand(int maxSize) {
        this.cards = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    public boolean addCard(List<Card> deck) {
    	IO.println(this.cards.size() + deck.size());
        if (this.cards.size() + deck.size()-1 < this.maxSize) {
            this.cards.addAll(deck);
            return true; 
        }
        return false; 
    }
    
    public List<Card> discardHand() {
        var hand = new ArrayList<>(this.cards);
    	cards.clear();
        return hand;
    }

	public List<Card> getCards() {
		return cards;
	}

    
    
    
}