package view.zen6.screens;

import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import view.zen6.Button;
import event.InputEvent.*;

public final class MainScreen extends UIScreen {
    public Button quitButton = null;

    public MainScreen(ApplicationContext context){
        Objects.requireNonNull(context);

        int width = 300, height = 100;
        ScreenInfo si = context.getScreenInfo();
        int X = si.width()/2 - width/2;
        int Y = si.height()/2 - height/2;

        var startButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.BLIND_SELECTION, null), "START GAME", X, Y, width, height, 1);
        var quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "QUIT", 30, si.height() - 100, 150, 80, 10);

        this.quitButton = quitButton;

        getUIObjects().add(startButton);
        getUIObjects().add(quitButton);
    }
}
