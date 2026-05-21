package view.zen6.screens;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import event.InputEvent.PlayerAction;
import view.zen6.Button;

public class EndScreen extends UIScreen{
    private Button quitButton;

    public EndScreen(ApplicationContext context, String message) {
        super(0);

        int width = 250, height = 80;
        ScreenInfo si = context.getScreenInfo();
        int X = si.width()/2 - width/2;
        int Y = si.height()/2 - height/2;

        var playButton = new Button((ctrl) -> ctrl.startGame(), message ,X, Y, width ,height, 1);
        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "quit", 0, 0, 100, 100, 10);

        getUIObjects().add(playButton);
    }

    public EndScreen(BlindScreen blindScreen, ApplicationContext context, String message){
        this(context, message);

        this.quitButton = blindScreen.quitButton;
        getUIObjects().add(this.quitButton);
    }

    public EndScreen(BlindSelectionScreen blindSelectionScreen, ApplicationContext context, String message){
        this(context, message);

        this.quitButton = blindSelectionScreen.quitButton;
        getUIObjects().add(this.quitButton);
    }

    public static EndScreen fromScreen(UIScreen previousScreen, ApplicationContext context, String message){
        if(previousScreen == null){
            EndScreen newScreen = new EndScreen(context, message);
            newScreen.getUIObjects().add(newScreen.quitButton);
            return newScreen;
        }
        return switch(previousScreen){
            case BlindScreen b -> new EndScreen(b, context, message);
            case BlindSelectionScreen bs -> new EndScreen(bs, context, message);
            default -> {
                EndScreen newScreen = new EndScreen(context, message);
                newScreen.getUIObjects().add(newScreen.quitButton);
                yield newScreen;}
        };
    }
}
