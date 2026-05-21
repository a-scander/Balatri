package view.zen6.screens;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import view.zen6.Button;
import event.InputEvent.*;

public final class MainScreen extends UIScreen {
    public Button quitButton = null;

    public MainScreen(ApplicationContext context){
        super(0);
        getUIObjects().clear();
        int X = 10, Y = 10;
        int width = 250, height = 80;
        if(context != null){
            ScreenInfo si = context.getScreenInfo();
            X = si.width()/2 - width/2;
            Y = si.height()/2 - height/2;
        }

        var startButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.BLIND_SELECTION, null), "startGame", X, Y, width, height, 1);
        var quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "quit", 0, 0, 100, 100, 10);

        this.quitButton = quitButton;

        getUIObjects().add(startButton);
        getUIObjects().add(quitButton);
    }
}
