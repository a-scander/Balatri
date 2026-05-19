package view;

import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import model.Phase;
import view.zen6.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import controller.GameController;
import domain.Card;

public final class Zen6View implements View {
    private final List<UIObject> uiObjects = new ArrayList<>();
    private GameController controller;
    private ApplicationContext context;

    public Zen6View(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void launch(GameController controller) {
        Application.run(Color.WHITE, context -> {
            this.context = context;
            redraw();
            while (true) {
                var event = context.pollOrWaitEvent(10);
                processEvent(event, controller.getState());
            }
        });
    }

    @Override
    public void onEvent(GameEvent event, GameState state) {
        switch (event) {
            case HandPlayed hp -> refreshHand(state);
            case CardUnselected us -> {selectCards(us.changedCards(), false);}
            case CardSelected cs -> {selectCards(cs.changedCards(), true);}
            case HandDrawn hd -> {addCards(hd.changedCards());}
            case HandDiscarded hd -> {removeCards(hd.changedCards());} 
            case BlindBeaten _, BlindOnGoing _, GameOver _, GameWon _ -> {}
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
        graphics.clearRect(0, 0, context.getScreenInfo().height(), context.getScreenInfo().width());
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

    public void processEvent(Event event, GameState state) {
        // Process any queued actions from non-GUI threads (e.g., console)
        controller.processQueuedActions();
        
        switch (event) {
            case null:
                break;
            case KeyboardEvent ke:
                processKeyboardEvent(ke);
                break;

            case PointerEvent pe:
                processPointerEvent(pe, state);
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

    private void processPointerEvent(PointerEvent pe, GameState state) {
        if (pe.action() != PointerEvent.Action.POINTER_DOWN) {return;/* Might do something for semi-clicks */}
        var location = pe.location();
        UIObject clickedObject = getClickedObject(new Point(location.x(), location.y()));
        
        switch (clickedObject) {
            case UICard uiCard -> controller.onAction(PlayerAction.CARD_CHOSE, uiCard.getCard());
            case Button button -> button.onClick(controller, state);
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
            case GAME_OVER -> buildGameOverMenu();
            default -> {}
        }
        IO.println(uiObjects);
        redraw();
    }

    private void buildMainMenu() {
        uiObjects.clear();

        var startButton = new Button(
            (ctrl, gs) -> ctrl.onAction(PlayerAction.START_GAME, null), "startGame",
            400, 300, 250, 80, 1
        );
    
        addUIObject(startButton);
    }

    private void buildBlindSelectionMenu() {
        uiObjects.clear();

        var blindButton = new Button(
            (ctrl, gs) -> ctrl.onAction(PlayerAction.SELECT_BLIND , null), "Select this blind",
            400, 600, 250, 80, 1
        );
    
        addUIObject(blindButton);
    }

    private void buildBlindUI() {
        UIHandContainer hc = new UIHandContainer(
            20, 200, 1000, 200, 0
        );
    
        addUIObject(hc);
    
        var playButton = new Button(
            (ctrl, gs) -> ctrl.onAction(PlayerAction.PLAY_HAND, null), "playhand" ,
            20, 420, 200, 50, 1
        );
    
        addUIObject(playButton);
    }

    private void buildGameOverMenu(){}

    private void selectCards(List<Card> changedCards, boolean isSelected){/*
                HandContainer.selectCards(changedCards, isSelected);*/
    }

    private void addCards(List<Card> changedCards) {/*
        changedCards.forEach(HandContainer::addCard);
        HandContainer.recomputeCardsCoordinates();*/
    }

    private void removeCards(List<Card> removedCards){/*
        removedCards.forEach(HandContainer::removeCard);
        HandContainer.recomputeCardsCoordinates();*/
    }

    private void refreshHand(GameState state) {/*
        HandContainer.removeAllCards();
        state.getCurrentBlind().getHand().getCards().forEach(HandContainer::addCard);
        state.getCurrentBlind().getSelectedCards().forEach(HandContainer::addCard);
        HandContainer.recomputeCardsCoordinates();*/
    }
}