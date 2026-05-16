package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hand {
    private List<Card> cardsHand;
    private int maxSize; 
    

    public Hand(int maxSize) {
        this.cardsHand = new ArrayList<>();
        this.maxSize = maxSize;
    }

    // Adds a list of cards (drawn from the deck) to the hand if there is enough space.
    public boolean addCard(List<Card> deck) {

    	if (this.cardsHand.size() + deck.size() <= this.maxSize) {
            this.cardsHand.addAll(deck);
            return true; 
        }
        return false; 
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
    // Creates a new sorted copy of the hand
    public List<Card> sortHand() {
        var sortedHand = new ArrayList<>(cardsHand);
        CardUtils.sort(sortedHand);
        return Collections.unmodifiableList(sortedHand);
    }

    @Override
    public String toString() {
        return "[" + sortHand().stream()
                .map(Card::toString)
                .collect(Collectors.joining(", ")) + "]";
    }    
}