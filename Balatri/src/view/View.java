package view;

import controller.GameController;
import event.OutputEvent.GameEvent;

public sealed interface View permits Zen6View, ConsoleView {
    public void onEvent(GameEvent event);

    public void launch(GameController controller);
}