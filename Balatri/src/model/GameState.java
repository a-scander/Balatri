package model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import domain.*;
import event.OutputEvent.*;

public class GameState {
	private final Blind[] blinds;
	private int blindIndex;
	private Blind currentBlind;
	
	private final Map<Planet, Integer> planetsObtained = new EnumMap<>(Planet.class);
	/*int Mooooney, 
	* List<Jokers>(A mettre dans blind peut être), 
	* List<Comsumable> (TarotCard and planet in comsumable area) 
	*/
	private Phase phase;
	
	public GameState() {
		/* Test values */
		this.blinds = new Blind[] {
			new Blind("Small Blind", BlindType.SMALL_BLIND, 30),
			new Blind("Big Blind",   BlindType.BIG_BLIND,   60) 
			// new Blind("Boss Blind",  BlindType.BOSS_BLIND,  120)
		};
		this.blindIndex = 0;
		this.currentBlind = blinds[blindIndex];
		this.phase = Phase.INITIALIZE;
	}

	public Blind getCurrentBlind()			{ return currentBlind; }
	public Map<Planet, Integer> getPlanetsObtained() { return planetsObtained; }
	public int getBlindIndex()				{ return blindIndex; }
	public Phase getPhase()					{ return phase; }
	public void setPhase(Phase phase)		{ this.phase = phase; }
	public GameEvent drawHand()				{ return new HandDrawn(currentBlind.drawHand()); }

	public GameEvent selectCard(Card card) {
		//TODO: if size is good select else message
		//if (currentblind.maxselectSize < currentblind.getSelectedCards().size() || modifiers has changed mas selection size)
		return currentBlind.selectCard(card);
	}

	public GameEvent onPlayHand() {
		if (currentBlind.getSelectedCards().isEmpty()) {
			return null;
		}
		//TODO: appliquer les jokers qui s'executent avant
		HandType handType = HandEvaluator.evaluate(currentBlind.getSelectedCards()); // TODO : return a handresult with the handtype and the scoring cards
		Planet planet = Planet.fromHandType(handType);
		int nbTimesObtained = planetsObtained.getOrDefault(planet, 0);
		int chips = handType.getBaseChips() + planet.getBonusChips() * nbTimesObtained;
		int mult = handType.getBaseMult() + planet.getBonusMult() * nbTimesObtained;
		//TODO : routine de scoring des cards et des jokers qui s'executent pendant
		//TODO : appliquer les jokers qui s"executent apres
		int score =  chips * mult;
		var discardedCards = new ArrayList<>(currentBlind.getSelectedCards());
		currentBlind.playHand(score);

		//DEBUG: 
		IO.println("Hand played: " + handType + " for " + score + " points. Total score: " + currentBlind.getScore());
		return new HandPlayed(score, handType, discardedCards, currentBlind.drawHand());
	}

	public GameEvent checkOutcome() {
		if(currentBlind.blindIsLost()) {
			return new GameOver();
		}

		if(currentBlind.getScore() < currentBlind.getTargetScore()) {
			return new BlindOnGoing();
		}

		if (blindIndex < blinds.length - 1) {
				blindIndex++;
				currentBlind = blinds[blindIndex];
				return new BlindBeaten();
		}
		
		return new GameWon();
	}

    public GameEvent onDiscard() {
        currentBlind.discard();
        return new HandDiscarded(currentBlind.drawHand());
	}

    public GameEvent onQuitGame() {
        //TODO : saving the current state and game in the controller
		return new GameOver();
    }
}
