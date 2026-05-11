import java.awt.Color;

import domain.CardUtils;
import domain.Deck;
import domain.Hand;

import java.awt.Graphics2D;

import com.github.forax.zen.Application;
import static com.github.forax.zen.Application.run;
import com.github.forax.zen.Event;
import com.github.forax.zen.ApplicationContext;

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

			run(Color.BLACK, context -> Main.render(context));
	}
	public static void render(ApplicationContext context){
		while (true) {
			Event event;
			while ((event = context.pollEvent()) != null) {
				System.out.println(event);
			}

			context.renderFrame((Graphics2D g) -> {
			g.setColor(Color.RED);
			g.fillRect(100, 100, 200, 200);
			});
		}
	}
}
