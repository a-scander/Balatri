package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import event.OutputEvent.CardSelected;
import event.OutputEvent.CardUnselected;
import event.OutputEvent.GameEvent;

public class Blind {
    private final String name;
    private final BlindType type;
    private final int targetScore;
    private int score;
	private int handsCurrent;
	private final int handsPerBlind = 4;
	private int discardCurrent;
    private final int discardPerBlind = 4;
	private final Deck deck;
    private final List<Card> discard;
    private final Hand hand;
    private final List<Card> selectedCards;


    public Blind(String name, BlindType type, int targetScore) {
        Objects.requireNonNull(name, "Blind name cannot be null");
        Objects.requireNonNull(type, "Blind type cannot be null");
        if (targetScore <= 0) {
            throw new IllegalArgumentException("Target score must be positive");
        }
        this.name = name;
        this.type = type;
        this.targetScore = targetScore;
        this.score = 0;
        this.handsCurrent = handsPerBlind;
        this.discardCurrent = discardPerBlind;
        this.deck = new Deck();
        this.hand = new Hand(8);
        this.selectedCards   = new ArrayList<>();
        this.discard = new ArrayList<>();
    }

    public String getName() {return name;}
    public List<Card> getSelectedCards() {return selectedCards;}
    public int getScore() {return score;}
    public int getHandsCurrent() {return handsCurrent;}
    public int getDiscardCurrent() {return discardCurrent;}
    public Hand getHand() {return hand;}
    public int getTargetScore() {return targetScore;}

    public GameEvent selectCard(Card card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            hand.getCards().add(card);
            return new CardUnselected(List.of(card));
        }

        selectedCards.add(card);
        hand.getCards().remove(card);
        return new CardSelected(List.of(card));
    }

    public boolean blindIsLost(){
        return handsCurrent >= handsPerBlind && score < targetScore;
    }

    public void playHand(int score){
        this.score += score;
        handsCurrent++;
        discard.addAll(selectedCards);
        selectedCards.clear();
        // discardFullHand();
    }

    public List<Card> drawHand() {
        List<Card> drawnCards = deck.drawCards(hand.remainingSpace());
        hand.getCards().addAll(drawnCards);
        return drawnCards;
    }

    public void discard() {
        discardCurrent++;
        discard.addAll(selectedCards);
        selectedCards.clear();
    }

    public void discardFullHand() {
        discardCurrent++;
        discard.addAll(hand.getCards());
        hand.getCards().clear();
    }

    @Override
    public String toString() {
        return name + " (cible : " + targetScore + ")";
    }
}