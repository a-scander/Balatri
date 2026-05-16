package view;

import controller.GameController;
import event.AppEvent;
import model.GameState;

public interface View  {
    public void onEvent(AppEvent event, GameState state);

    public void launch(GameController controller);
}