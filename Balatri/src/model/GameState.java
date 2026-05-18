package model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import domain.*;
import event.OutputEvent.BlindBeaten;
import event.OutputEvent.GameEvent;
import event.OutputEvent.GameOver;
import event.OutputEvent.GameWon;
import event.OutputEvent.HandDiscarded;
import event.OutputEvent.HandDrawn;
import event.OutputEvent.HandPlayed;


public class GameState {
	private final Blind[] blinds;
	private int blindIndex;
	private Blind currentBlind;
	
	private final Map<Planet, Integer> planetsObtained;
	/*int Mooooney, List<Jokers>(A mettre dans blind peut être), List<Comsumable> (TarotCard and planet in comsumable area) */
	private Phase phase;

	
	public GameState() {
		this.blinds = new Blind[] {
			new Blind("Small Blind", BlindType.SMALL_BLIND, 30),
			new Blind("Big Blind",   BlindType.BIG_BLIND,   60) 
			// new Blind("Boss Blind",  BlindType.BOSS_BLIND,  120)
		};
		this.blindIndex = 0;
		this.planetsObtained = new EnumMap<>(Planet.class);
		this.currentBlind = blinds[blindIndex];
		this.phase = Phase.INITIALIZE;
	}

	public enum Phase {
		INITIALIZE,
		MAIN_SCREEN,
		IN_SHOP,
		BLIND_SELECTION,
		IN_BLIND,
		GAME_OVER
	}

	public Blind getCurrentBlind()           { return currentBlind; }
	public Map<Planet, Integer> getPlanetsObtained() { return planetsObtained; }
	public int getBlindIndex()               { return blindIndex; }
	public Phase getPhase() 				 { return phase; }
	public void setPhase(Phase phase) 		 { this.phase = phase; }

	public GameEvent selectCard(Card card) {

		//TODO: if size is good select else message
		//if (currentblind.maxselectSize < currentblind.getSelectedCards().size() || modifiers has changed mas selection size)
		return currentBlind.selectCard(card);
	}

	public GameEvent drawHand(){
		return new HandDrawn(currentBlind.drawHand());
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

		//DEBUG: IO.println("Hand played: " + handType + " for " + score + " points. Total score: " + currentBlind.getScore());
		return new HandPlayed(score, handType, discardedCards, currentBlind.drawHand());
	}

	public GameEvent checkOutcome() {
		if(currentBlind.blindIsLost()) {
			return new GameOver();
		}
		
		if (blindIndex < blinds.length - 1) {
			blindIndex++;
			currentBlind = blinds[blindIndex];
			return new BlindBeaten();
		} else {
			return new GameWon();
		}
	}

    public GameEvent onDiscard() {
        currentBlind.discard();
        return new HandDiscarded(currentBlind.drawHand());
	}

    public GameEvent onQuitGame() {
        //TODO : saving the current state and game
		return new GameOver();
    }
}
