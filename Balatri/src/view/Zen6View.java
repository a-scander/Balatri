package view;

import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import model.Phase;
import view.zen6.*;
import view.zen6.screens.*;
import controller.GameController;
import domain.Card;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.awt.Color;
import java.awt.Graphics2D;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

public final class Zen6View implements View {
    private GameController controller;
    private ApplicationContext context;
    UIScreen screen = null;
    BiConsumer<PlayerAction, Object> queueAction;
    private volatile boolean running = true;

    public Zen6View(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void launch(GameController controller, BiConsumer<PlayerAction, Object> queueAction) {
        this.queueAction = queueAction;
        this.screen = new MainScreen(null);
        queueAction.accept(PlayerAction.START_GAME, null);
        Application.run(Color.WHITE, context -> {
            this.context = context;
            redraw();
            while (running) {
                var event = context.pollOrWaitEvent(10);
                processEvent(event);
            }
        });
    }

    @Override
    public void onEvent(GameEvent event) {
        switch (event) {

            case HandChangeEvent hce -> processHandChangeEvent(hce);
            
            case BlindBeaten _, BlindOnGoing _ -> {}
            case GameOver _ -> {screen = EndScreen.fromScreen(null, context, "You Lost");}
            case GameWon _ -> {screen = EndScreen.fromScreen(null, context, "You Win");}
            case PhaseChange pc -> buildPhaseUI(pc.phase());
            case GameClosed _ -> {IO.println("Game closed.");running = false;context.dispose();System.exit(0);}
        }
        redraw();
    }

    public void processHandChangeEvent(HandChangeEvent event){
        switch(screen){
            case BlindScreen BScreen -> {
                switch(event){
                    case HandPlayed hp -> { //let the warning it's for when cards will score individually
                        GameState state = controller.getState();
                        List<Card> cards = Stream.concat(
                            state.getCurrentBlind().getHand().getCards().stream(),
                            state.getCurrentBlind().getSelectedCards().stream()).toList();
                        BScreen.getUiHandContainer().refreshHand(cards);
                    }
                    case CardUnselected us -> {BScreen.getUiHandContainer().selectCards(us.changedCards(), false);}
                    case CardSelected cs -> {if(cs.changedCards() == null){break;}BScreen.getUiHandContainer().selectCards(cs.changedCards(), true);}
                    case HandDrawn hd -> {BScreen.getUiHandContainer().addCards(hd.changedCards());}
                    case HandDiscarded hd -> {BScreen.getUiHandContainer().removeCards(hd.changedCards());} 
                }   
            }
            default -> {return;}
        }
    }

    private void drawFrame(Graphics2D graphics) {
        drawBackground(graphics);
        screen.render(graphics);
    }

    private void redraw() {
        if(this.context == null)return;
        context.renderFrame(this::drawFrame);
    }

    private void drawBackground(Graphics2D graphics){
        graphics.clearRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
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
            this.queueAction.accept(PlayerAction.QUIT_GAME, null);
            running = false;
            context.dispose();
        }
        if(ke.key() == KeyboardEvent.Key.SPACE){
            this.queueAction.accept(PlayerAction.PLAY_HAND, null);
            redraw();
        }
    }

    private void processPointerEvent(PointerEvent pe) {
        if (pe.action() != PointerEvent.Action.POINTER_DOWN) {return;/* Might do something for semi-clicks */}
        var location = pe.location();
        UIObject clickedObject = screen.getClickedObject(new Point(location.x(), location.y()));
        
        switch (clickedObject) {
            case UICard uiCard -> this.queueAction.accept(PlayerAction.CARD_CHOSE, uiCard.getCard());
            case Button button -> button.onClick(controller);
            case UIRectangle _, UIHandContainer _, UIBlind _, InfoMenu _-> {}
            case null -> {}
        }
        redraw();
    }

    private void buildPhaseUI(Phase phase) {
        switch (phase) {
            case MAIN_SCREEN -> screen = new MainScreen(context);
            case BLIND_SELECTION -> screen = BlindSelectionScreen.fromScreen(screen, controller.getState());
            case IN_BLIND -> screen = BlindScreen.fromScreen(screen, controller.getState());
            case GAME_OVER -> {}//screen = new EndScreen(context, "you lose");
            default -> {}
        }
        redraw();
    }
}