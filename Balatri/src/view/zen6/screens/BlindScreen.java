package view.zen6.screens;

import event.InputEvent.PlayerAction;
import model.GameState;
import view.zen6.Button;
import view.zen6.InfoMenu;
import view.zen6.UIHandContainer;

public class BlindScreen extends UIScreen {
    public Button quitButton;
    public UIHandContainer uiHandContainer;

    public InfoMenu infoMenu; //Remember to refresh on state change and on HandChangEvent
    
    //public UIDeck uiDeck; //with a deckMenu (to display the whole deck with or whithout discard)

    //public ConsumableContainer comsumableContainer;

    //public JokerContainer jokerContainer;

    public BlindScreen(GameState state){
        this.uiHandContainer = new UIHandContainer(
            700, 450, 1180, 250, 0
        );

        this.infoMenu = new InfoMenu(30, 30, 580, 700, 5, state);
    
        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "QUIT", 30, 450, 150, 80, 10);
    }

    public UIHandContainer getUiHandContainer(){return uiHandContainer;}

    public BlindScreen(BlindSelectionScreen blindSelectionScreen, GameState state){
        this(state);

        this.quitButton = blindSelectionScreen.quitButton;
        getUIObjects().add(this.quitButton);

        this.infoMenu = blindSelectionScreen.infoMenu;
        getUIObjects().add(infoMenu);
        getUIObjects().addAll(infoMenu.getObjects());


        /*if(blindScreen.jokerContainer != null){
            jokerContainer = blindScreen.jokerContainer;
        }*/
    }

    public static BlindScreen fromScreen(UIScreen previousScreen, GameState state){
        if(previousScreen == null){
            BlindScreen newScreen = new BlindScreen(state);
            return newScreen;
        }

        BlindScreen newScreen = switch(previousScreen){
            case BlindSelectionScreen b -> new BlindScreen(b, state);
            default -> new BlindScreen(state);
        };

        newScreen.getUIObjects().add(newScreen.quitButton);
        newScreen.getUIObjects().add(newScreen.infoMenu);
        newScreen.getUIObjects().addAll(newScreen.infoMenu.getObjects());
        newScreen.getUIObjects().add(newScreen.uiHandContainer);
        newScreen.getUIObjects().addAll(newScreen.uiHandContainer.getObjects());

        return newScreen;
    }
}
