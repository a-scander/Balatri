package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> deckCards;
	private List<Card> discardCards;


	public Deck() {
		this.deckCards = new ArrayList<>();
		this.discardCards = new ArrayList<>();
		initializeDeck();
	}
	
	private void initializeDeck() {
		for (Suit suit : Suit.values()) { 
			for (Rank rank : Rank.values()) { 
				this.deckCards.add(new Card(rank, suit));
			}
		}
		Collections.shuffle(this.deckCards);
	}
	
	public List<Card> drawCards(int n) {
        if (deckCards.size() < n) {
        	deckCards.addAll(discardCards);
        	discardCards.clear();
            Collections.shuffle(deckCards);
        }
        List<Card> drawn = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            drawn.add(deckCards.remove(deckCards.size() - 1));
        }
        return drawn;
    }
	
	public void discardCards(List<Card> cards) {
		discardCards.addAll(cards);
	}

	public List<Card> deckCards() {
		return deckCards;
	}

	public List<Card> getDiscardCards() {
		return discardCards;
	}

	
	
	
	
	
}