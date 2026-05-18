import model.GameState;
import view.*;
import controller.GameController;

public class Main {
	public static void main(String[] args) {
		GameState state     = new GameState();
		GameController ctrl = new GameController(state);

		// Link the view to the controller
		if(args.length > 0 && args[0].equals("console")) {
			ctrl.addView(new ConsoleView(ctrl));
		} else{
			ctrl.addView(new Zen6View(ctrl));
			ctrl.addView(new ConsoleView(ctrl)); /*Multi-Threading test */
		}

		ctrl.startGame();
		ctrl.launch();
	}
}
