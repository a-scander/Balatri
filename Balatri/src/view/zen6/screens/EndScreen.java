package view.zen6.screens;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import event.InputEvent.PlayerAction;
import view.zen6.Button;

public class EndScreen extends UIScreen{
    private Button quitButton;

    public EndScreen(ApplicationContext context, String message) {
        int width = 300, height = 100;
        ScreenInfo si = context.getScreenInfo();
        int X = si.width()/2 - width/2;
        int Y = si.height()/2 - height/2;

        var playButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.START_GAME, null), message ,X, Y, width ,height, 1); //conteext isn't initialize on first screen
        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "QUIT", 30, si.height() - 100, 150, 80, 10);

        getUIObjects().add(playButton);
    }

    public EndScreen(BlindScreen blindScreen, ApplicationContext context, String message){
        this(context, message);

        this.quitButton = blindScreen.quitButton;
    }
    public static EndScreen fromScreen(UIScreen previousScreen, ApplicationContext context, String message){
        if(previousScreen == null){
            EndScreen newScreen = new EndScreen(context, message);
            newScreen.getUIObjects().add(newScreen.quitButton);
            return newScreen;
        }
        EndScreen newScreen = switch(previousScreen){
            case BlindScreen b -> new EndScreen(b, context, message);
            default -> new EndScreen(context, message);
        };

        newScreen.getUIObjects().add(newScreen.quitButton);
        return newScreen;

    }
}
