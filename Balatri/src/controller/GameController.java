package controller;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import domain.*;
import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import model.GameState.Phase;
import view.ConsoleView;
import view.View;
import view.Zen6View;

public class GameController {

    private static record PendingAction(PlayerAction action, Object data) {}

    private final GameState state;
    private final List<View> views = new ArrayList<>();
    private final ConcurrentLinkedQueue<PendingAction> actionQueue = new ConcurrentLinkedQueue<>();

    public GameController(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void addView(View view) {
        this.views.add(view);
    }

    public void launch() {
        if (views.isEmpty()) {
            throw new IllegalStateException("A view must be assigned before launching the game.");
        }

        // Launch any non-GUI views first so they can process input concurrently.
        for (View view : views) {
            if (view instanceof Zen6View) {
                continue;
            }

            Thread t = new Thread(() -> {
                try {
                    view.launch(this);
                } catch (Throwable e) {
                    IO.println("View " + view.getClass().getSimpleName() + " failed to launch: " + e);
                    e.printStackTrace();
                }
            }, "ViewLauncher-" + view.getClass().getSimpleName());
            t.setDaemon(false);
            t.start();
        }

        // Run the GUI view on the main thread if present.
        for (View view : views) {
            if (view instanceof Zen6View) {
                try {
                    view.launch(this);
                } catch (Throwable e) {
                    IO.println("View " + view.getClass().getSimpleName() + " failed to launch on main thread: " + e);
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    private void emit(GameEvent event) {
        for(View view : views){
            view.onEvent(event, state);
        }
    }

    public void startGame() {
        if (views.isEmpty()) {
            throw new IllegalStateException("View must be assigned before starting the game.");
        }
        emit(state.drawHand());
        state.setPhase(GameState.Phase.MAIN_SCREEN);
    }

    private void initializeGame() {
        /*once the user request playing a game */
        /*displays blind selection */
        state.setPhase(GameState.Phase.BLIND_SELECTION);
        /*emit(PHASE_CHANGED_EVENT) */
    }

    private void startBlind() {
        /*once the user has selected the blind starts it */
        /*Should initialize the blind */
        //TODO: this is only a test, should be replaced by the actual blind initialization logic
        emit(state.drawHand());
        state.setPhase(GameState.Phase.IN_BLIND);


        //state.setPhase(GameState.Phase.IN_BLIND);
    }

    private void finishGame() {
    }

    public void queueAction(PlayerAction action, Object data) {
        actionQueue.offer(new PendingAction(action, data));
    }

    public void processQueuedActions() {
        PendingAction pending;
        while ((pending = actionQueue.poll()) != null) {
            onAction(pending.action, pending.data);
        }
    }

    public void onAction(PlayerAction action, Object data) {
        switch (action) {
            case CARD_CHOSE -> {emit(state.selectCard((Card) data));}
            case PLAY_HAND -> {
                GameEvent playResult = state.onPlayHand();
                //IO.println("Play hand result: " + playResult);
                if(playResult == null){
                    IO.println("No cards selected, cannot play hand.");
                    return;
                }
                emit(playResult);
                GameEvent outcome = state.checkOutcome();
                switch(outcome){
                    case BlindBeaten _ -> state.setPhase(Phase.BLIND_SELECTION);/*TODO: blind changing logic and shop */
                    case BlindOnGoing _ -> state.setPhase(Phase.IN_BLIND);
                    case GameOver _ -> state.setPhase(Phase.GAME_OVER);
                    case GameWon _ -> state.setPhase(Phase.GAME_OVER);
                    default -> {}
                    //TODO : calculate money won and apply jokers that execute on last hand
                }
                emit(outcome);
            }
            
            case DISCARD -> emit(state.onDiscard());
            case QUIT_GAME -> emit(state.onQuitGame());
            /*case all phase changes
            * case START_GAME -> initializeGame();
            * case BLIND_SELECTED -> startBlind();
            * case SELECT_BLIND -> startBlind();
            */
        }
    }

    
}
