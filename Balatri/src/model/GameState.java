package model;

import java.util.ArrayList;
import java.util.List;

import domain.*;


public class GameState {
    	private final Blind[] blinds;
	    private int blindIndex;
	    private int score;
	    private int handsCurrent;
	    private final int handsPerBlind = 4;
		private int discardCurrent;
	    private final int discardPerBlind = 4;
		private final Deck deck;
	    private final Hand hand;
	    private final List<Planet> planetsObtained;
		private final List<Card> selectedCards;
	    
	    public GameState() {
			this.blinds = Blind.values();
			this.blindIndex = 0;
			this.score = 0;
			this.handsCurrent = handsPerBlind;
			this.discardCurrent = discardPerBlind;
			this.deck= new Deck();
			this.hand = new Hand(8);
			this.planetsObtained = new ArrayList<>();
			this.selectedCards   = new ArrayList<>();

		}

		public Blind getCurrentBlind()           { return blinds[blindIndex]; }
		public int getScore()                    { return score; }
		public int getHandsCurrent()             { return handsCurrent; }
		public int getDiscardCurrent()           { return discardCurrent;}
		public Deck getDeck()                    { return deck; }
		public Hand getHand()                    { return hand; }
		public List<Planet> getPlanetsObtained() { return planetsObtained; }
		public List<Card> getSelectedCards()     { return selectedCards; }
		public int getBlindIndex()               { return blindIndex; }
		public int getScoreBlindCurrent() 		 {return this.getCurrentBlind().getTargetScore();}
		public boolean isSelected(Card card)     {return selectedCards.contains(card);}
		public void addSelectedCard(Card card)   { this.selectedCards.add(card); }
		public void removeSelectedCard(Card card){ this.selectedCards.remove(card); }
		public void addScore(int points)         { this.score += points; }
		public void decrementHands()             { this.handsCurrent--; }
		public void decrementDiscard()			 { this.discardCurrent--;}
		public void nextBlind()                  { this.blindIndex++; score = 0; handsCurrent = handsPerBlind; discardCurrent = discardPerBlind;}
		public void clearSelection()             { this.selectedCards.clear(); }

		// Draws 'n' cards from the deck and adds them directly to the player's hand.
		public void drawCards(int n) {
			List<Card> drawn = getDeck().drawCards(n);
			getHand().addCard(drawn);
		}

		// Discards the entire hand to the deck pile and clears the current selection.
		public void discardFullHand() {
			List<Card> all = hand.discardHandAll();
			deck.discardCards(all);
			clearSelection();
		}
		
		// Discards only the selected cards, sends them to the deck discard pile, and draws replacement cards.
		public void discardSelected() {
			List<Card> discarded = hand.discardHand(selectedCards);
			deck.discardCards(discarded);
			clearSelection();
			drawCards(discarded.size());
		}
		
}
