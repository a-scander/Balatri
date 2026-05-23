package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import event.OutputEvent.CardSelected;
import event.OutputEvent.CardUnselected;
import event.OutputEvent.GameEvent;
/*Not a simple class but still in domain package, maybe to move to another one */
public class Blind {
    private final String name;
    private final BlindType type; // Will be useful once we implement special effects blind ? 
    private final int targetScore;
    private int score;
	private int handsCurrent;
	private final int handsPerBlind = 4;
	private int discardCurrent;
    private final int discardPerBlind = 4;
	private final Deck deck;
    private final List<Card> discard = new ArrayList<>();
    private final Hand hand;
    private final List<Card> selectedCards = new ArrayList<>();

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
        this.handsCurrent = 0; // Should not be a constant
        this.discardCurrent = 0; // Should not be a constant
        this.deck = new Deck();
        this.hand = new Hand(8); // Should not be a constant
    }

    public String getName()                 { return name;}
    public List<Card> getSelectedCards()    { return selectedCards;}
    public int getScore()                   { return score;}
    public int getHandsCurrent()            { return handsCurrent;}
    public int getDiscardCurrent()          { return discardCurrent;}
    public Hand getHand()                   { return hand;}
    public int getTargetScore()             { return targetScore;}
    public boolean blindIsLost()            { return handsCurrent >= handsPerBlind && score < targetScore;}
    public BlindType getType()              { return type;}
    public int getHandPerBlind(){return handsPerBlind;}
    public int getDiscardPerBlind(){return discardPerBlind;}
    public int getRemainingHandNb(){return handsPerBlind - handsCurrent;}
    public int getRemainingDiscardNb(){return discardPerBlind - discardCurrent;}

    public GameEvent selectCard(Card card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            hand.getCards().add(card);
            return new CardUnselected(List.of(card)); // Maybe List.of is Overkilled but otherwise more complex in classes inheritace so loose/loose
        }
        if(selectedCards.size() > 4)return new CardSelected(null);

        selectedCards.add(card);
        hand.getCards().remove(card);
        return new CardSelected(List.of(card));
    }

    public void playHand(int score){
        this.score += score;
        handsCurrent++;
        discard.addAll(selectedCards);
        selectedCards.clear();
    }

    public List<Card> drawHand() {
        List<Card> drawnCards = deck.drawCards(hand.remainingSpace());
        hand.getCards().addAll(drawnCards);
        return drawnCards;
    }

    public void discard() {
        if(discardCurrent >= discardPerBlind)return;
        discardCurrent++;
        discard.addAll(selectedCards);
        selectedCards.clear();
    }

    public void discardFullHand() {
        if(discardCurrent >= discardPerBlind)return;
        discardCurrent++;
        discard.addAll(hand.getCards());
        hand.getCards().clear();
    }

    @Override
    public String toString() {
        return name + " (cible : " + targetScore + ")";
    }
}