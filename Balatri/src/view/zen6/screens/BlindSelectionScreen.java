package view.zen6.screens;

import java.util.List;

import domain.Blind;
import event.InputEvent.PlayerAction;
import model.GameState;
import view.zen6.Button;
import view.zen6.InfoMenu;
import view.zen6.UIBlind;

public class BlindSelectionScreen extends UIScreen {
    public Button quitButton; //TODO: remove once infoMenu is set

    public InfoMenu infoMenu;
    
    //public UIDeck uiDeck;

    //public ConsumableContainer comsumableContainer;

    //public JokerContainer jokerContainer;

    public List<UIBlind> UIblinds = new java.util.ArrayList<>(); //Should be constant to size 3

    public BlindSelectionScreen(GameState state) {
        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "QUIT", 30, 450, 150, 80, 10);

        this.infoMenu = new InfoMenu(30, 30, 580, 700, 5, state);

        var blinds = state.getBlinds();
        for(int i = state.blindIndex / 3; i < 3; i++){
            int y = (i == state.blindIndex % 3) ? 200 : 220;
            var UIblind = new UIBlind(750 + i * 240, y, 220, 320, 1, blinds.get(i), i == state.blindIndex % 3);
            this.UIblinds.add(UIblind);
            getUIObjects().add(UIblind);
            getUIObjects().add(UIblind.selectButton);
        }
    }

    public void refreshBlinds(GameState state){
        var blinds = state.getBlinds();
        for(int i = 0; i < blinds.size(); i++){
            var blind = this.UIblinds.get(i);
            blind.blind = blinds.get(i);
        }
    }

    public BlindSelectionScreen(MainScreen mainScreen, GameState state){
        this(state);

        this.quitButton = mainScreen.quitButton;
    }

    public BlindSelectionScreen(BlindScreen blindScreen, GameState state){
        this(state);

        this.quitButton = blindScreen.quitButton;
        this.infoMenu = blindScreen.infoMenu;
        this.infoMenu.reset();

        /*if(blindScreen.jokerContainer != null){
            jokerContainer = blindScreen.jokerContainer;
        }*/
    }

    public static BlindSelectionScreen fromScreen(UIScreen previousScreen, GameState state){
        if(previousScreen == null){
            BlindSelectionScreen newScreen = new BlindSelectionScreen(state);
            newScreen.getUIObjects().add(newScreen.quitButton);
            return newScreen;
        }

        BlindSelectionScreen newScreen = switch(previousScreen){
            case MainScreen m -> new BlindSelectionScreen(m, state);
            case BlindScreen b -> new BlindSelectionScreen(b, state);
            default -> new BlindSelectionScreen(state);
        };

        newScreen.getUIObjects().add(newScreen.quitButton);
        newScreen.getUIObjects().add(newScreen.infoMenu);
        newScreen.getUIObjects().addAll(newScreen.infoMenu.getObjects());
        return newScreen;
    }
}
