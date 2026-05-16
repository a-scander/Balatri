package view.zen6;

import domain.Card;
import event.AppEvent;
import event.GameEvent;
import event.PlayerAction;
import event.ZenEvent;
import model.GameState;
import view.View;
import view.ui.CardUI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.*;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import controller.GameController;

public class Zen6View implements View {
    private final List<UIObject> uiObjects = new ArrayList<>();
    private GameController controller;
    private ApplicationContext context;

    public static Zen6View initGameGraphics(GameState state, GameController controller) {
        var view = new Zen6View();
        view.controller = controller;
        return view;
    }

    public boolean isUICard(UIObject obj) {
        return switch(obj){
            case UICard _ -> true;
            default -> false;
        };
    }

    private void refreshHandCards(GameState state) {
        var cards = state.getHand().getCards();
        var selectedCards = state.getSelectedCards();

        // Rebuild the hand UI cards so positions and z-depth stay consistent.
        uiObjects.removeIf(this::isUICard);

        for (int index = 0; index < cards.size(); index++) {
            var card = cards.get(index);
            int x = 50 + index * 110;
            int y = selectedCards.contains(card) ? 420 : 400;
            UICard uiCard = new UICard(card, x, y, 100, 150, index);
            uiObjects.add(uiCard);
        }

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
    
    public UIObject getClickedObject(Point location) {
        return uiObjects.stream()
            .filter(obj -> obj.contains(location))
            .max(Comparator.comparingInt(UIObject::zDepth))
            .orElse(null);
    }

    @Override
    public void onEvent(AppEvent event, GameState state) {
        switch(event){
            case ZenEvent ze -> processEvent(ze.event(), state);
            case GameEvent ge -> processGameEvent(ge, state);
        }
    }

    private void processGameEvent(GameEvent event, GameState state) {
        switch (event) {
            case HAND_DRAWN, HAND_PLAYED -> refreshHandCards(state);
            case DISCARD_SELECTED, CARD_SELECTED, SELECTION_HAND -> refreshHandCards(state);
            case BLIND_BEATEN, GAME_OVER, GAME_WON -> {}
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
                switch (clickedObject) {
                    case UICard uiCard -> controller.onAction(PlayerAction.CARD_CHOSE, uiCard.getCard());
                    case Button button -> button.callBack(state);
                    case UIRectangle uiRect -> {}
                    case null -> {}
                }
                redraw();

        }
    }

    @Override
    public void launch(GameController controller) {
        controller.drawHand();
        Application.run(Color.WHITE, context -> BalatriGame(context, controller));
    }

    public void BalatriGame(ApplicationContext context, GameController controller) {
        this.context = context;
        redraw();
        while (true) {
            var event = context.pollOrWaitEvent(10);
            ZenEvent gameEvent = new ZenEvent(event);
            onEvent(gameEvent, controller.getState());
        }
    }
}