package view;

import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import model.Phase;
import view.zen6.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.awt.Color;
import java.awt.Graphics2D;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.ScreenInfo;

import controller.GameController;
import domain.Card;

public final class Zen6View implements View {
    private final List<UIObject> uiObjects = new ArrayList<>();
    private GameController controller;
    private ApplicationContext context;

    private UIHandContainer uiHandContainer = null;

    public Zen6View(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void launch(GameController controller) {
        Application.run(Color.WHITE, context -> {
            this.context = context;
            buildMainMenu();
            redraw();
            while (true) {
                var event = context.pollOrWaitEvent(10);
                processEvent(event);
            }
        });
    }

    @Override
    public void onEvent(GameEvent event) {
        switch (event) {
            case HandPlayed hp -> {
                GameState state = controller.getState();
                List<Card> cards = Stream.concat(
                    state.getCurrentBlind().getHand().getCards().stream(),
                    state.getCurrentBlind().getSelectedCards().stream()).toList();
                uiHandContainer.refreshHand(cards);
            }
            case CardUnselected us -> {uiHandContainer.selectCards(us.changedCards(), false);}
            case CardSelected cs -> {if(cs.changedCards() == null){break;}uiHandContainer.selectCards(cs.changedCards(), true);}
            case HandDrawn hd -> {uiHandContainer.addCards(hd.changedCards());}
            case HandDiscarded hd -> {uiHandContainer.removeCards(hd.changedCards());} 
            case BlindBeaten _, BlindOnGoing _ -> {}
            case GameOver _ -> {buildEndMenu("You Lost");}
            case GameWon _ -> {buildEndMenu("You Win");}
            case PhaseChange pc -> buildPhaseUI(pc.phase());
        }
        redraw();
    }

    private void drawFrame(Graphics2D graphics) {
        drawBackground(graphics);
        for (UIObject obj : getUIObjects()) {
            obj.draw(graphics);
        }
    }

    private void redraw() {
        if(this.context == null)return;
        context.renderFrame(this::drawFrame);
    }

    public void addUIObject(UIObject obj) {uiObjects.add(obj);}
    
    public void removeUIObject(UIObject obj) {uiObjects.remove(obj);}

    public void clearUIObjects(){uiObjects.clear();};
    
    public List<UIObject> getUIObjects() {return new ArrayList<>(uiObjects);}

    private void drawBackground(Graphics2D graphics){
        graphics.clearRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
    }
    
    private UIObject updateBest(UIObject candidate, UIObject currentBest, int[] bestZ) {
        if (candidate.zDepth() > bestZ[0]) {
            bestZ[0] = candidate.zDepth();
            return candidate;
        }
        return currentBest;
    }
    
    public UIObject getClickedObject(Point location) {
        UIObject best = null;
        int[] bestZ = { Integer.MIN_VALUE };
        for (UIObject obj : uiObjects) {
            if (!obj.contains(location)) {continue;}
    
            best = updateBest(obj, best, bestZ);
            if (obj instanceof UIHandContainer c) { // this or ugly pattern matching :/ (PM better when more UIObjects)
                for (UICard card : c.getCards()) {
                    if (card.contains(location)) {
                        best = updateBest(card, best, bestZ);
                    }
                }
            }
        }
        return best;
    }

    public void processEvent(Event event) {
        // Process any queued actions from non-GUI threads (e.g., console)
        controller.processQueuedActions();
        
        switch (event) {
            case null:
                break;
            case KeyboardEvent ke:
                processKeyboardEvent(ke);
                break;

            case PointerEvent pe:
                processPointerEvent(pe);
                break;
        }
    }

    private void processKeyboardEvent(KeyboardEvent ke) {
        if (ke.action() != KeyboardEvent.Action.KEY_PRESSED) {return;}
        // maybe a switch ?
        if(ke.key() == KeyboardEvent.Key.Q){
            controller.onAction(PlayerAction.QUIT_GAME, null);
            context.dispose();
        }
        if(ke.key() == KeyboardEvent.Key.SPACE){
            controller.onAction(PlayerAction.PLAY_HAND, null);
            redraw();
        }
    }

    private void processPointerEvent(PointerEvent pe) {
        if (pe.action() != PointerEvent.Action.POINTER_DOWN) {return;/* Might do something for semi-clicks */}
        var location = pe.location();
        UIObject clickedObject = getClickedObject(new Point(location.x(), location.y()));
        
        switch (clickedObject) {
            case UICard uiCard -> controller.onAction(PlayerAction.CARD_CHOSE, uiCard.getCard());
            case Button button -> button.onClick(controller);
            case UIRectangle _, UIHandContainer _ -> {}
            case null -> {}
        }
        redraw();
    }

    private void buildPhaseUI(Phase phase) {
        uiObjects.clear();
        switch (phase) {
            case MAIN_SCREEN -> buildMainMenu();
            case BLIND_SELECTION -> buildBlindSelectionMenu();
            case IN_BLIND -> buildBlindUI();
            case GAME_OVER -> {}
            default -> {}
        }
        redraw();
    }

    private void buildMainMenu() {
        uiObjects.clear();
        uiHandContainer = null;
        int X = 10, Y = 10;
        int width = 250, height = 80;
        if(this.context != null){
            ScreenInfo si = this.context.getScreenInfo();
            X = si.width()/2 - width/2;
            Y = si.height()/2 - height/2;
        }

        var startButton = new Button(
            (ctrl) -> ctrl.startGame(), "startGame", X, Y, width, height, 1);
    
        addUIObject(startButton);
    }

    private void buildBlindSelectionMenu() {
        uiObjects.clear();
        uiHandContainer = null;

        var blindButton = new Button(
            (ctrl) -> ctrl.onAction(PlayerAction.SELECT_BLIND , null), "Select this blind",
            400, 300, 250, 80, 1);
    
        addUIObject(blindButton);
    }

    private void buildBlindUI() {
        this.uiHandContainer = new UIHandContainer(
            20, 200, 1000, 200, 0
        );
    
        addUIObject(this.uiHandContainer);
    
        var playButton = new Button(
            (ctrl) -> ctrl.onAction(PlayerAction.PLAY_HAND, null), "playhand" ,
            20, 420, 200, 50, 1
        );
    
        addUIObject(playButton);
    }

    private void buildEndMenu(String message){
        uiObjects.clear();
        uiHandContainer = null;

        int width = 250, height = 80;
        ScreenInfo si = this.context.getScreenInfo();
        int X = si.width()/2 - width/2;
        int Y = si.height()/2 - height/2;

        var playButton = new Button(
            (ctrl) -> ctrl.startGame(), message ,
            X, Y, width ,height, 1
        );

        addUIObject(playButton);
    }
}