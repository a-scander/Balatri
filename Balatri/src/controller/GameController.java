package controller;

import domain.*;
import event.InputEvent.*;
import event.OutputEvent.*;
import event.OutputEvent.GameEvent;
import model.GameState;
import model.GameState.Phase;
import view.View;

public class GameController {

    private final GameState state;
    private View view;

    public GameController(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void launch() {
        if (view == null) {
            throw new IllegalStateException("View must be assigned before launching the game.");
        }
        view.launch(this);
        
    }

    private void emit(GameEvent event) {
        if (view != null) {
            view.onEvent(event, state);
        }
    }

    public void startGame() {
        if (view == null) {
            throw new IllegalStateException("View must be assigned before starting the game.");
        }
        emit(state.drawHand());
        state.setPhase(GameState.Phase.MAIN_SCREEN);
    }

    private void initializeGame() {
        /*once the user request playing a game */
        /*initialize the blinds */
        state.setPhase(GameState.Phase.BLIND_SELECTION);
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

    public void onAction(PlayerAction action, Object data) {
        switch (action) {
            case CARD_CHOSE -> {emit(state.selectCard((Card) data));}
            case PLAY_HAND -> {
                GameEvent playResult = state.onPlayHand();
                IO.println("Play hand result: " + playResult);
                if(playResult == null){
                    IO.println("No cards selected, cannot play hand.");
                    return;
                }
                emit(playResult);
                GameEvent outcome = state.checkOutcome();
                switch(outcome){
                    case BlindBeaten _ -> state.setPhase(Phase.BLIND_SELECTION);
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
