package view;

import controller.GameController;
import event.InputEvent.PlayerAction;
import event.OutputEvent.GameEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;

public sealed interface View permits Zen6View, ConsoleView {
    public void onEvent(GameEvent event);

    public void launch(GameController controller, BiConsumer<PlayerAction, Object> queueAction);
}