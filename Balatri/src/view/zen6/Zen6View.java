package view.zen6;

import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import view.View;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import controller.GameController;
import domain.Card;

public class Zen6View implements View {
    private final List<UIObject> uiObjects = new ArrayList<>();
    private GameController controller;
    private ApplicationContext context;
    private UIHandContainer HandContainer;

    public static Zen6View initGameGraphics(GameState state, GameController controller) { /*TODO: make a contructor instead */
        var view = new Zen6View();
        view.controller = controller;
        view.HandContainer = new UIHandContainer(50, 300, 800, 200, 0);
        view.addUIObject(view.HandContainer);
        return view;
    }

    public boolean isUICard(UIObject obj) {
        return switch(obj){
            case UICard _ -> true;
            default -> false;
        };
    }

    private void selectCards(List<Card> changedCards, boolean isSelected){
        HandContainer.selectCards(changedCards, isSelected);
    }

    private void addCards(List<Card> changedCards) {
        changedCards.forEach(HandContainer::addCard);
        HandContainer.recomputeCardsCoordinates();
        redraw();
    }

    private void removeCards(List<Card> removedCards){
        removedCards.forEach(HandContainer::removeCard);
        HandContainer.recomputeCardsCoordinates();
        redraw();
    }

    private void refreshHand(GameState state) {
        HandContainer.removeAllCards();
        state.getCurrentBlind().getHand().getCards().forEach(HandContainer::addCard);
        state.getCurrentBlind().getSelectedCards().forEach(HandContainer::addCard);
        HandContainer.recomputeCardsCoordinates();
        redraw();
    }

    private void drawFrame(Graphics2D graphics) {
        //IO.println(uiObjects.stream().filter(this::isUICard).map(obj -> ((UICard) obj).getCard()).toList());
        var clip = graphics.getClipBounds();
        if (clip != null) {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
        }

        for (UIObject obj : getUIObjects()) {
            obj.draw(graphics);
        }
    }
    
    public void draw(ApplicationContext context) {
        this.context = context;
        context.renderFrame(this::drawFrame);
    }

    private void redraw() {
        if (context != null) {
            context.renderFrame(this::drawFrame);
        }
    }

    public void addUIObject(UIObject obj) {
        uiObjects.add(obj);
    }
    
    public void removeUIObject(UIObject obj) {
        uiObjects.remove(obj);
    }
    
    public List<UIObject> getUIObjects() {
        return new ArrayList<>(uiObjects);
    }
    
    public UIObject getClickedObject(Point location) {//to change
        UIObject best = null;
        int bestZ = Integer.MIN_VALUE;

        for (UIObject obj : uiObjects) {
            if (!obj.contains(location)) continue;

            if (obj instanceof UIHandContainer hc) {
                UICard clicked = hc.getClickedCard(location);
                if (clicked != null) {
                    if (clicked.zDepth() > bestZ) {
                        best = clicked;
                        bestZ = clicked.zDepth();
                    }
                }
                if (obj.zDepth() > bestZ) {
                    best = obj;
                    bestZ = obj.zDepth();
                }
            } else {
                if (obj.zDepth() > bestZ) {
                    best = obj;
                    bestZ = obj.zDepth();
                }
            }
        }

        return best;
    }

    @Override
    public void onEvent(GameEvent event, GameState state) {
        switch (event) {
            case HandPlayed hp -> refreshHand(state);
            case CardUnselected us -> {selectCards(us.changedCards(), false);}
            case CardSelected cs -> {selectCards(cs.changedCards(), true);}
            case HandDrawn hd -> {addCards(hd.changedCards());}
            case HandDiscarded hd -> {removeCards(hd.changedCards());} 
            case BlindBeaten _, GameOver _, GameWon _ -> {}
        }
        redraw();
    }

    public void processEvent(Event event, GameState state) {
        switch (event) {
            case null:
                break;
            case KeyboardEvent ke:
                if (ke.action() != KeyboardEvent.Action.KEY_PRESSED) {return;}
                if(ke.key() == KeyboardEvent.Key.Q){
                    controller.onAction(PlayerAction.QUIT_GAME, null);
                }
                if(ke.key() == KeyboardEvent.Key.SPACE){
                    controller.onAction(PlayerAction.PLAY_HAND, null);
                    redraw();

                }

                break;
            case PointerEvent pe:
                if (pe.action() != PointerEvent.Action.POINTER_DOWN) {return;}
                var location = pe.location();
                UIObject clickedObject = getClickedObject(new Point(location.x(), location.y()));
                System.out.println("PointerEvent at " + location.x() + "," + location.y() + " -> clicked: " + clickedObject);
                switch (clickedObject) {
                    case UICard uiCard -> controller.onAction(PlayerAction.CARD_CHOSE, uiCard.getCard());
                    case Button button -> button.callBack(state);
                    case UIRectangle _, UIHandContainer _ -> {}
                    case null -> {}
                }
                redraw();

        }
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
}