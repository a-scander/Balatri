package model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import domain.*;
import domain.configs.Config;
import domain.configs.FlushConfig;
import domain.configs.StraightConfig;
import domain.jokers.*;
import event.OutputEvent.*;

public class GameState {
	private final Blind[] blinds;
	public int blindIndex;
	private Blind currentBlind;

	public int handsPerBlind = 4;
	public int discardsPerBlind = 4;
	
	private final Map<Planet, Integer> planetsObtained = new EnumMap<>(Planet.class);
	public FlushConfig flushConfig;
	public StraightConfig straightConfig;
	public Map<JokerType, List<Joker>> jokers;

	/*int Mooooney, 
	
	* List<Comsumable> (TarotCard and planet in comsumable area) 
	*/
	private Phase phase;
	
	public GameState() {
		/* Test values */
		this.blinds = new Blind[] {
			new Blind("Small Blind", handsPerBlind, discardsPerBlind, BlindType.SMALL_BLIND, 100),
			new Blind("Big Blind", handsPerBlind, discardsPerBlind, BlindType.BIG_BLIND,   150),
			new Blind("Boss Blind", handsPerBlind, discardsPerBlind, BlindType.BOSS_BLIND,  200)
		};

		for (var planet : Planet.values()) {
            planetsObtained.put(planet, 0);
        }

		this.blindIndex = 0;//Math.MIN
		this.currentBlind = null; //this is not to do at initialization but at blind_selection event call should be null
		this.phase = Phase.INITIALIZE;
	}

	public List<Config> getConfigs(){
		return List.of(straightConfig, flushConfig /*... */);
	}

	public Blind getCurrentBlind()			{ return currentBlind; }
	public Map<Planet, Integer> getPlanetsObtained() { return planetsObtained; }
	public Phase getPhase()					{ return phase; }
	public void setPhase(Phase phase)		{ this.phase = phase; }
	public GameEvent drawHand()				{ return new HandDrawn(currentBlind.drawHand()); }
	public List<Blind> getBlinds()			{ return List.of(blinds); }
	public void addPlanet(Planet p)					{ planetsObtained.merge(p, 1, Integer::sum);}

	public GameEvent selectCard(Card card) {
		return currentBlind.selectCard(card);
	}

	public GameEvent onPlayHand() {
		HandResult result = getSelectedHandType();
		if(result == null)return null;
		HandType handType = result.type();

		Score newScore = getModifiedHandTypeValue(handType);
		//TODO : routine de scoring des cards et des jokers qui s'executent pendant
		//TODO : appliquer les jokers qui s"executent apres
		int score = (newScore.chips() + CardUtils.sumChips(result.scoringCards())) * newScore.mult();
		var discardedCards = new ArrayList<>(currentBlind.getSelectedCards());
		currentBlind.playHand(score);

		//DEBUG: 
		IO.println("Hand played: " + handType + " for " + score + " points. Total score: " + currentBlind.getScore());
		return new HandPlayed(score, handType, discardedCards, currentBlind.drawHand());
	}

	public HandResult getSelectedHandType(){
		//TODO: appliquer les jokers qui s'executent avant
		return HandEvaluator.evaluate(currentBlind.getSelectedCards() /*getConfigs */); // TODO : return a handresult with the handtype and the scoring cards
	}

	public GameEvent checkOutcome() {
		if(currentBlind.blindIsLost()) {
			return new GameOver();
		}

		if(currentBlind.getScore() < currentBlind.getTargetScore()) {
			return new BlindOnGoing();
		}

		if (blindIndex < blinds.length - 1) {
			return new BlindBeaten();
		}
		
		return new GameWon();
	}

    public GameEvent onDiscard() {
		if(currentBlind.getRemainingDiscardNb() == 0){return null;}
        currentBlind.discard();
        return new HandDiscarded(currentBlind.drawHand());
	}

    public GameEvent onQuitGame() {
		return new GameOver();
    }

	public Score getModifiedHandTypeValue(HandType handType){
		Planet planet = Planet.fromHandType(handType);
		//if(handType == null)return new Score(0, 0);
		int nbTimesObtained = planetsObtained.get(planet);
		Score baseScore = handType.getScore();
		Score planetScoreMod = planet.getScore();
		return new Score(baseScore.chips() + planetScoreMod.chips() * nbTimesObtained, 
									baseScore.mult() + planetScoreMod.mult() * nbTimesObtained);
	}

	public int getHandLevel(HandType handType){
		Planet planet = Planet.fromHandType(handType);
		return planetsObtained.get(planet) + 1;
	}

	public Score scoreCards(HandResult result){
		for(var card : result.scoringCards()){
			for(var joker : this.jokers.get(JokerType.DURING)){
				/*if(joker.activates()){ //TODO
					joker.apply(this);
				}*/
			}
		}
		return new Score(0, 0); //TODO: return the score of the cards with the jokers applied
		/*
		for(var card : currrentBlind.getHand()){
			for(var joker : this.jokers.get(JokerType.DURING)){
				if(joker.activates()){ //TODO
					joker.apply(this);
				}
			}
		}
		*/
	}

	public void startBlind(){
		this.currentBlind = blinds[blindIndex];
	}

	public void endBlind(){
		IO.println("ENNDED");
		this.blindIndex++;
		this.currentBlind = null;
	}
}
