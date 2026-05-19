package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import domain.*;
import event.InputEvent.*;
import event.OutputEvent.*;
import model.GameState;
import model.Phase;
import view.ConsoleView;
import view.View;
import view.Zen6View;

public class GameController {
    private static record PendingAction(PlayerAction action, Object data) {}
    private final ConcurrentLinkedQueue<PendingAction> actionQueue = new ConcurrentLinkedQueue<>();

    private final GameState state;
    private final List<View> views = new ArrayList<>();

    public GameController(GameState state) {this.state = state;}
    public GameState getState() {return state;}
    public void addView(View view) {this.views.add(view);}

    public void launch() {
        if (views.isEmpty()) {
            throw new IllegalStateException("A view must be assigned before launching the game.");
        }

        // Launch non-GUI views in background threads.
        for (View view : views) {
            switch (view) {
                case Zen6View _ -> {} // Skip GUI views for now
                case ConsoleView consoleView -> {
                    Thread t = new Thread(() -> {consoleView.launch(this);}, "ViewLauncher-ConsoleView");
                    t.setDaemon(false);
                    t.start();
                }
            }
        }

        // Run the GUI view on the main thread.
        for (View view : views) {
            switch(view){
                case Zen6View zen6View -> {
                    zen6View.launch(this);
                    return;} //Stops at the first GUI view, we don't want to launch multiple GUI views
                default -> {}
            }
        }
    }

    private void emit(GameEvent event) {
        for(View view : views){
            view.onEvent(event, state);
        }
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
            case CARD_CHOSE -> { emit(state.selectCard((Card) data));}
            case PLAY_HAND -> { playHand();}
            case DISCARD -> emit(state.onDiscard());
            case QUIT_GAME -> emit(state.onQuitGame());
            case START_GAME -> initializeGame();
            case SELECT_BLIND -> startBlind();
            /*TODO: add phase changes
            * case START_GAME -> initializeGame();
            * case BLIND_SELECTED -> startBlind();
            * case SELECT_BLIND -> startBlind();
            */
        }
    }

    public void playHand() {
        GameEvent playResult = state.onPlayHand();
        //IO.println("Play hand result: " + playResult);
        if(playResult == null){
            IO.println("No cards selected, cannot play hand.");
            return;
        }

        emit(playResult);

        GameEvent outcome = state.checkOutcome();
        switch(outcome){
            case BlindBeaten _ -> changePhase(Phase.BLIND_SELECTION);

            case BlindOnGoing _ -> changePhase(Phase.IN_BLIND);

            case GameOver _ -> changePhase(Phase.GAME_OVER);

            case GameWon _ -> changePhase(Phase.GAME_OVER);
            default -> {}
        }
        emit(outcome);
    }

    public void startGame() {
        if (views.isEmpty()) {
            throw new IllegalStateException("View must be assigned before starting the game.");
        }
        
        changePhase(Phase.MAIN_SCREEN);
    }

    private void initializeGame() {
        /*once the user request playing a game */
        /*displays blind selection */
        changePhase(Phase.BLIND_SELECTION);
    }

    private void startBlind() {
        /*once the user has selected the blind starts it */
        /*Should initialize the blind */
        //TODO: this is only a test, should be replaced by the actual blind initialization logic
        emit(state.drawHand());
        changePhase(Phase.IN_BLIND);
    }

    private void finishGame() {
    }
    
    private void changePhase(Phase phase) {
        state.setPhase(phase);
        emit(new PhaseChange(phase));
    }
}
