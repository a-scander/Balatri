package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Hand {
    private List<Card> cards;
    private int maxSize; 

    public Hand(int maxSize) {
        this.cards = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    public boolean addCard(List<Card> deck) {
    	if (this.cards.size() + deck.size() <= this.maxSize) {
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

    public List<Card> sortHand() {
        var sortedHand = new ArrayList<>(cards);
        sortedHand.sort(Comparator.comparingInt(card -> card.rank().getValue()));
        return Collections.unmodifiableList(sortedHand);
    }

    @Override
    public String toString() {
        return "[" + sortHand().stream()
                .map(Card::toString)
                .collect(Collectors.joining(", ")) + "]";
    }
    
    
}