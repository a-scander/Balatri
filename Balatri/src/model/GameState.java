package model;

import java.util.ArrayList;
import java.util.List;

import domain.Blind;
import domain.Deck;
import domain.Hand;
import domain.Planet;

public class GameState {
    	private final Blind[] blinds;
	    private int blindIndex;
	    private int score;
	    private int handsCurrent;
	    private final int handsPerBlind = 4;
		private final Deck deck;
	    private final Hand hand;
	    private final List<Planet> planetsObtained;
	    
	    public GameState() {
			this.blinds = Blind.values();
			this.blindIndex = 0;
			this.score = 0;
			this.handsCurrent = handsCurrent;
			this.deck= new Deck();
			this.hand = new Hand(8);
			this.planetsObtained = new ArrayList<>();
		}

	
}
