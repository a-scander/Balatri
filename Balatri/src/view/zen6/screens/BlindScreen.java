package view.zen6.screens;

import event.InputEvent.PlayerAction;
import model.GameState;
import view.zen6.Button;
import view.zen6.InfoMenu;
import view.zen6.UIHandContainer;

public class BlindScreen extends UIScreen {
    public Button quitButton;
    public UIHandContainer uiHandContainer;// TODO: add the sort buttons and play and discard button to the UIHandContainer class 

    public InfoMenu infoMenu; //Remember to refresh on state change and on HandChangEvent
    
    //public UIDeck uiDeck; //with a deckMenu (to display the whole deck with or whithout discard)

    //public ConsumableContainer comsumableContainer;

    //public JokerContainer jokerContainer;

    public BlindScreen(GameState state){
        super(0);

        this.uiHandContainer = new UIHandContainer(
            20, 200, 1000, 200, 0
        );
    
        getUIObjects().add(this.uiHandContainer);
    
        var playButton = new Button(
            (ctrl) -> ctrl.queueAction(PlayerAction.PLAY_HAND, null), "playhand" ,
            20, 420, 200, 50, 1
        );

        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "quit", 0, 100, 100, 100, 10);

        getUIObjects().add(playButton);
    }

    public UIHandContainer getUiHandContainer(){return uiHandContainer;}

    public BlindScreen(BlindSelectionScreen blindSelectionScreen, GameState state){
        this(state);

        this.quitButton = blindSelectionScreen.quitButton;
        getUIObjects().add(this.quitButton);

        /*if(blindScreen.jokerContainer != null){
            jokerContainer = blindScreen.jokerContainer;
        }*/
    }

    public static BlindScreen fromScreen(UIScreen previousScreen, GameState state){
        if(previousScreen == null){
            BlindScreen newScreen = new BlindScreen(state);
            newScreen.getUIObjects().add(newScreen.quitButton);
            return newScreen;
        }

        return switch(previousScreen){
            case BlindSelectionScreen b -> new BlindScreen(b, state);
            default -> {
                BlindScreen newScreen = new BlindScreen(state);
                newScreen.getUIObjects().add(newScreen.quitButton);
                yield newScreen;}
        };
    }
}
