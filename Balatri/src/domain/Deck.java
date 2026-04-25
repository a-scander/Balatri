package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> pickaxeCards;
	private List<Card> discardCards;


	public Deck() {
		this.pickaxeCards = new ArrayList<>();
		this.discardCards = new ArrayList<>();
		initializeDeck();
	}
	
	private void initializeDeck() {
		for (Suit suit : Suit.values()) { 
			for (Rank rank : Rank.values()) { 
				this.pickaxeCards.add(new Card(rank, suit));
			}
		}
		Collections.shuffle(this.pickaxeCards);
	}
	
	
	public List<Card> drawCards(int n) {
        if (pickaxeCards.size() < n) {
        	pickaxeCards.addAll(discardCards);
        	discardCards.clear();
            Collections.shuffle(pickaxeCards);
        }
        List<Card> drawn = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            drawn.add(pickaxeCards.remove(pickaxeCards.size() - 1));
        }
        return drawn;
    }
	
	 public void discardCards(List<Card> cards) {
		 discardCards.addAll(cards);
	    }

	 public List<Card> getPickaxeCards() {
		 return pickaxeCards;
	 }


	 public List<Card> getDiscardCards() {
		 return discardCards;
	 }

	
	
	
	
	
}