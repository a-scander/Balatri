package view;

import domain.Card;
import event.OutputEvent.BlindBeaten;
import event.OutputEvent.CardSelected;
import event.OutputEvent.GameEvent;
import event.OutputEvent.GameOver;
import event.OutputEvent.GameWon;
import event.OutputEvent.HandDrawn;
import event.OutputEvent.HandPlayed;
import model.GameState;

import java.util.Scanner;
import controller.GameController;

public class ConsoleView implements View {

    private GameController controller;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleView(GameController controller) {
        this.controller = controller;
    }

    // Handles incoming game engine events and routes them to specific UI display methods.
    @Override
    public void onEvent(GameEvent event, GameState state) {
        /*switch(event) {
            case HandDrawn _       -> onHandDrawn(state);
            case SelectionHand _   -> askPlayer(state);
            case CardSelected _    -> onCardSelected(state);
            case HandPlayed _      -> onHandPlayed(state);
            case BlindBeaten _     -> onBlindBeaten(state);
            case GameOver _        -> onGameOver();
            case GameWon _         -> onGameWon();
            default -> {}
        }*/
    }

    @Override
    public void launch(GameController controller) {
        controller.startGame();
    }

    private void displaySeparator() {
        System.out.println("\n-------------------------------------------------");
    }
    /* 
    private void displayBlindInfo(GameState state) {
        System.out.println("=== " + state.getCurrentBlind().getName() + " ===");
        System.out.println("Score : " + state.getScore() + " / " + state.getCurrentBlind().getTargetScore());
        System.out.println("Mains restantes : " + state.getHandsCurrent());
        System.out.println("Defausses restantes : " + state.getDiscardCurrent());
    }

    private void displayHand(GameState state) {
        System.out.println("\nTa main :");
        for (int i = 0; i < state.getHand().getCards().size(); i++) {
            System.out.println("[" + i + "] " + state.getHand().getCards().get(i));
        }
    }

    private void displaySelection(GameState state) {
        System.out.println("\nCartes selectionnees : " + state.getSelectedCards().size() + "/5");
        for (Card card : state.getSelectedCards()) {
            System.out.println("- " + card);
        }
    }

    private void displayGameInfo(GameState state) {
        displaySeparator();
        displayBlindInfo(state);
        displayHand(state);
        displaySelection(state);
    }

    private void onHandDrawn(GameState state) {
        displayGameInfo(state);
    }

    private void onCardSelected(GameState state) {
        askPlayer(state);
    }

    private void onHandPlayed(GameState state) {
        displaySeparator();
        displayBlindInfo(state);
    }

    private void onBlindBeaten(GameState state) {
        displaySeparator();
        System.out.println("Blind battu !");
    }

    private void onGameOver() {
        displaySeparator();
        System.out.println("GAME OVER");
    }

    private void onGameWon() {
        displaySeparator();
        System.out.println("VICTOIRE");
    }

    // Prompts the user for action choices and routes terminal inputs into specific actions.
    private void askPlayer(GameState state) {
        displayGameInfo(state);
        System.out.println("\nChoisis une carte (0-7) | P pour jouer | D pour defausser :");
        String input = scanner.nextLine().trim().toUpperCase();

        switch(input) {
            case "P" -> controller.onAction(PlayerAction.PLAY_HAND, null);
            case "D" -> handleDiscard(state);
            default  -> handleCardInput(state, input);
        }
    }

    // Validates remaining discard tokens before executing a discard action.
    private void handleDiscard(GameState state) {
        if (state.getDiscardCurrent() <= 0) {
            System.out.println("Plus de defausses disponibles !");
            askPlayer(state);
        } else {
            controller.onAction(PlayerAction.DISCARD, null);
        }
    }

    // Validates that alphanumeric inputs correspond to a valid integer card index.
    private void handleCardInput(GameState state, String input) {
        try {
            int index = Integer.parseInt(input);
            if (index < 0 || index >= state.getHand().getCards().size()) {
                System.out.println("Index invalide ! Choisis entre 0 et 7.");
                askPlayer(state);
            } else {
                handleCardSelection(state, index);
            }
        } catch (NumberFormatException e) {
            System.out.println("Entree invalide ! Tape un chiffre entre 0-7, P ou D.");
            askPlayer(state);
        }
    }
    
    // Handles the selection rules, enforcing a maximum limit of 5 selected cards.
    private void handleCardSelection(GameState state, int index) {
        Card card = state.getHand().getCards().get(index);
        if (state.getSelectedCards().size() >= 5 && !state.isSelected(card)) {
            System.out.println("Max 5 cartes ! Retire une carte ou appuie sur P/D.");
            askPlayer(state);
        } else {
            controller.onAction(PlayerAction.CARD_CHOSE, card);
        }
    }*/
}