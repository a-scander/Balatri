package controller;

import domain.*;
import event.*;
import model.GameState;

import view.View;


public class GameController {


    private final GameState state;
    private View view;

    public GameController(GameState state) {
        this.state = state;
        view = null;
    }

    // Assigns a view to the controller to establish the MVC communication.
    public void setView(View view) {
        this.view = view;
    }

    // Sends an event to the view to trigger a UI refresh with the current state.
    private void emit(GameEvent event) {
        this.view.onEvent(event, state);
    }

    // Starts the main game loop 
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
            // Case when a player clicks/selects an individual card.
            case CARD_CHOSE -> onCardChoose((Card) data);
            
            // Case when the player decides to play their selected poker hand.
            case PLAY_HAND -> playHandSelected();
            
            // Case when the player decides to discard the selected cards.
            case DISCARD ->  discardHandSelected();      
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
       
        HandType handType = HandEvaluator.evaluate(state.getSelectedCards());
        int score = handType.getBaseChips() * handType.getBaseMult();
        state.addScore(score);
        state.decrementHands();
        state.discardFullHand();
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