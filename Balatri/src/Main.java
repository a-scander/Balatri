

import model.GameState;
import view.*;
import controller.GameController;

public class Main {

	public static void main(String[] args) {
			// Deck deck = new Deck();
			// Hand hand = new Hand(25);
			// hand.addCard(deck.drawCards(25));
			

			// IO.println(hand.getCards());
			
			// deck.discardCards(hand.discardHand());
			// IO.println(hand.getCards());
			
			// IO.println("pioche : " + CardUtils.cardsToString(deck.deckCards()));
			// IO.println("defausse : " + CardUtils.cardsToString(deck.getDiscardCards()));

			// run(Color.BLACK, context -> Main.render(context));
		GameState state     = new GameState();
		GameController ctrl = new GameController(state);

		View view;
		// Link the view to the controller
		if(args.length > 0 && args[0].equals("console")) {
			ctrl.addView(new ConsoleView(ctrl));
		} else{
			view = Zen6View.initGameGraphics(state, ctrl);
			ctrl.addView(view);
		}
		ctrl.addView(new ConsoleView(ctrl));
		ctrl.startGame();
		ctrl.launch();
	}
}
