package controller;

import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import domain.*;
import event.*;
import model.GameState;
import view.View;
import view.zen6.Button;
import view.zen6.Point;
import view.zen6.UICard;
import view.zen6.UIObject;
import view.zen6.Zen6View;


public class GameController {

    private final GameState state;
    private View view;
    private final Object stateLock = new Object();

    public GameController(GameState state) {
        this.state = state;
    }

    public GameState getState(){
        return state;
    }

    // Assigns a view to the controller to establish the MVC communication.
    public void setView(View view) {
        this.view = view;
    }

    // Sends an event to the view to trigger a UI refresh with the current state.
    private void emit(AppEvent event) {
        view.onEvent(event, state);
    }

    // Starts the main game loop.
    public void startGame() {
        while (true) {

            drawHand();
            startSelection(); 
    
            // Check if the final boss blind is beaten and the entire game is won.
            if (winGame()) {
                emit(GameEvent.GAME_WON);
                break;
            }
            
            // Check if the player has run out of hands without beating the blind score.
            if (loseGame()) {
                emit(GameEvent.GAME_OVER);
                break;
            }
            
            // Check if the current score is enough to clear the current round/blind.
            if (winBlind()) {
                emit(GameEvent.BLIND_BEATEN);
                state.nextBlind();
            }
        }
    }

    // Refills the player's hand up to 8 cards and notifies the view.
    public void drawHand() {
        state.drawCards(8);
        emit(GameEvent.HAND_DRAWN);
    }

    // Triggers the event indicating the player is currently selecting cards.
    private void startSelection() {
        emit(GameEvent.SELECTION_HAND);
    }

    // Handles user interactions sent from the view based on the action type.
    public void onAction(PlayerAction action, Object data) {
        switch(action) {
            case CARD_CHOSE -> onCardChoose((Card) data);
            case PLAY_HAND -> playHandSelected();
            case DISCARD -> discardHandSelected();
            case QUIT_GAME -> {
                emit(GameEvent.GAME_OVER);
                System.exit(0);
            }
        }
    }
       
    // Toggles the selection status of a card: deselects it if already chosen, or selects it if under the 5-card limit.
    private void onCardChoose(Card card) {
        if (state.getSelectedCards().contains(card)) {
            state.removeSelectedCard(card);
        } else if (state.getSelectedCards().size() < 5) {
            state.addSelectedCard(card);
        }
        emit(GameEvent.CARD_SELECTED);
    }

    // Evaluates the scoring of the selected hand, updates game state values, and discards used cards.
    private void playHandSelected() {
        if(state.getSelectedCards().isEmpty()) {
            IO.println("No cards selected to play.");
            return;
        }
        HandType handType = HandEvaluator.evaluate(state.getSelectedCards());
        int score = handType.getBaseChips() * handType.getBaseMult();
        state.addScore(score);
        IO.println("Hand played: " + handType + " for " + score + " points. Total score: " + state.getScore());
        state.decrementHands();
        state.discardFullHand();
        state.drawCards(state.getHand().remainingSpace());
        emit(GameEvent.HAND_PLAYED);
    }
    
    // Discards the currently selected cards and consumes one discard.
    private void discardHandSelected() {
        state.discardSelected();
        state.decrementDiscard();
        emit(GameEvent.DISCARD_SELECTED);
    }
  
    // Verifies if the player's total score has met or exceeded the current blind target.
    private boolean winBlind(){
        return state.getScore() >= state.getScoreBlindCurrent();
    }
    
    // Determines game victory if the player beats the final blind available in the enum list.
    private boolean winGame() {
        return state.getBlindIndex() >= Blind.values().length - 1 && winBlind();
    }
    
    // Determines game loss if the player has 0 remaining hands and hasn't reached the required target score.
    private boolean loseGame() {
        return state.getHandsCurrent() == 0 && !winBlind();
    }
    
}