package view;

import event.GameEvent;
import model.GameState;

public interface View  {
    void onEvent(GameEvent event, GameState state);

}