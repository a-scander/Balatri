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
    private final ConcurrentLinkedQueue<PendingAction> actionQueue = new ConcurrentLinkedQueue<>();

    private GameState state;
    private final List<View> views = new ArrayList<>();

    public GameController() {}
    public GameState getState() {return state;}
    public void addView(View view) {this.views.add(view);}

    public void launch() {

        //Should replace launch(this, queueAction)
        //By launch(this, () -> queueAction; processQueuedAction; )
        if (views.isEmpty()) {
            throw new IllegalStateException("A view must be assigned before launching the game.");
        }
        if(views.size() == 1){
            views.getFirst().launch(this, (action, data) -> {
                        queueAction(action, data); processQueuedActions();});
            return;
        }

        // Launch non-GUI views in background threads.
        for (View view : views) {
            switch (view) {
                case Zen6View _ -> {} // Skip GUI views for now
                case ConsoleView consoleView -> {
                    Thread t = new Thread(() -> {consoleView.launch(this, this::queueAction);}, "ViewLauncher-ConsoleView");
                    t.setDaemon(false);
                    t.start();
                }
            }
        }

        // Run the GUI view on the main thread.
        for (View view : views) {
            switch(view){
                case Zen6View zen6View -> {
                    zen6View.launch(this, (action, data) -> {
                        queueAction(action, data); processQueuedActions();}
                    );
                    return;} //Stops at the first GUI view, we don't want to launch multiple GUI views
                default -> {}
            }
        }
    }

    private void emit(GameEvent event) {
        for(View view : views){
            view.onEvent(event);
        }
    }

    public void queueAction(PlayerAction action, Object data) {
        actionQueue.offer(new PendingAction(action, data));
    }

    public void processQueuedActions() {
        PendingAction pending;
        while ((pending = actionQueue.poll()) != null) {
            onAction(pending.action(), pending.data());
        }
    }

    public void onAction(PlayerAction action, Object data) {
        switch (action) {
            case CARD_CHOSE -> { emit(state.selectCard((Card) data));}
            case PLAY_HAND -> { playHand();}
            case DISCARD -> emit(state.onDiscard());
            case QUIT_GAME -> emit(state.onQuitGame());
            case START_GAME -> startGame();
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
        if(playResult == null){
            IO.println("No cards selected, cannot play hand.");
            return;
        }

        emit(playResult);

        GameEvent outcome = state.checkOutcome();
        switch(outcome){
            case BlindBeaten _ -> {if(state.getPhase() != Phase.BLIND_SELECTION)changePhase(Phase.BLIND_SELECTION);}

            case BlindOnGoing _ -> {if(state.getPhase() != Phase.IN_BLIND)changePhase(Phase.IN_BLIND);}

            case GameOver _ -> {if(state.getPhase() != Phase.GAME_OVER)changePhase(Phase.GAME_OVER);}

            case GameWon _ -> {if(state.getPhase() != Phase.GAME_OVER)changePhase(Phase.GAME_OVER);}
            default -> {}
        }
        emit(outcome);
    }

    public void startGame() {
        this.state = new GameState();
        changePhase(Phase.BLIND_SELECTION);
    }

    private void initializeGame() {
        /*once the user request playing a game */
        /*displays blind selection */
        changePhase(Phase.BLIND_SELECTION);
    }

    private void startBlind() {
        /*once the user has selected the blind starts it */
        /*Should initialize the blind properly*/
        changePhase(Phase.IN_BLIND);
        emit(state.drawHand());
    }

    private void finishGame() {
    }
    
    private void changePhase(Phase phase) {
        state.setPhase(phase);
        emit(new PhaseChange(phase));
    }
}
