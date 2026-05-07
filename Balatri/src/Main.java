import domain.CardUtils;
import domain.Deck;
import domain.Hand;

public class Main {

	public static void main(String[] args) {
			Deck deck = new Deck();
			Hand hand = new Hand(25);
			hand.addCard(deck.drawCards(25));
			

			IO.println(hand.getCards());
			
			deck.discardCards(hand.discardHand());
			IO.println(hand.getCards());
			
			IO.println("pioche : " + CardUtils.cardsToString(deck.deckCards()));
			IO.println("defausse : " + CardUtils.cardsToString(deck.getDiscardCards()));
	}
}
