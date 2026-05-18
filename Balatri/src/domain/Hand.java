package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cardsHand = new ArrayList<>();
    private int maxSize; 
    

    public Hand(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    // Clears the entire hand
    public List<Card> discardHandAll() {
        var hand = new ArrayList<>(this.getCards());
    	this.getCards().clear();
        return hand;
    }

    // Removes specific selected cards from the hand and returns them.
    public List<Card> discardHand(List<Card> selectedCards) {
        List<Card> discarded = new ArrayList<>(selectedCards); 
        cardsHand.removeAll(selectedCards);
        return discarded; 
    }

	public List<Card> getCards() {
		return cardsHand;
	}

    public void setCards(List<Card> newCards) {
        this.cardsHand.addAll(newCards);
    }
    
    // Creates a new sorted copy of the hand
    public List<Card> sortHand() {
        var sortedHand = new ArrayList<>(cardsHand);
        CardUtils.sort(sortedHand);
        return Collections.unmodifiableList(sortedHand);
    }

    public int remainingSpace(){
        return maxSize - cardsHand.size();
    }

    @Override
    public String toString() {
        return "[" + sortHand().stream()
                .map(Card::toString)
                .collect(Collectors.joining(", ")) + "]";
    }    
}