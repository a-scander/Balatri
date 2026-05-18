package view;

import controller.GameController;
import event.OutputEvent.GameEvent;
import model.GameState;

public sealed interface View permits Zen6View, ConsoleView {
    public void onEvent(GameEvent event, GameState state);

    public void launch(GameController controller);
}