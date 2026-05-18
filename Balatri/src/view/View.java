package view;

import controller.GameController;
import event.OutputEvent.GameEvent;
import model.GameState;

public interface View  {
    public void onEvent(GameEvent event, GameState state);

    public void launch(GameController controller);
}