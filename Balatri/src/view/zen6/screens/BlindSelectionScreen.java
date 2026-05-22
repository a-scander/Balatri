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
        super(0);
    
        this.quitButton = new Button((ctrl) -> ctrl.queueAction(PlayerAction.QUIT_GAME, null), "quit", 0, 100, 100, 100, 10);

        var blinds = state.getBlinds();
        
        for(int i = 0; i < blinds.size(); i++){
            Blind blind = blinds.get(i);
            int y = blind == state.getCurrentBlind() ? 300 : 320;
            var UIblind = new UIBlind(400 + i * 100, y, 80, 200, 1, blinds.get(i), blind == state.getCurrentBlind());
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
        getUIObjects().add(this.quitButton);
    }

    public BlindSelectionScreen(BlindScreen blindScreen, GameState state){
        this(state);

        this.quitButton = blindScreen.quitButton;
        getUIObjects().add(this.quitButton);

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

        return switch(previousScreen){
            case MainScreen m -> new BlindSelectionScreen(m, state);
            case BlindScreen b -> new BlindSelectionScreen(b, state);
            default -> {
                BlindSelectionScreen newScreen = new BlindSelectionScreen(state);
                newScreen.getUIObjects().add(newScreen.quitButton);
                yield newScreen;}
        };
    }
}
