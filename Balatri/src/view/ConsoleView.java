package view;

import domain.Card;
import event.InputEvent.PlayerAction;
import event.OutputEvent.*;
import model.GameState;

import java.util.Scanner;
import controller.GameController;

public final class ConsoleView implements View {

    private final GameController controller;
    private volatile boolean running = true;

    public ConsoleView(GameController controller) {
        this.controller = controller;
    }

    // Handles incoming game engine events and routes them to specific UI display methods.
    @Override
    public void onEvent(GameEvent event, GameState state) {
        switch(event) {
            case HandDrawn _       -> onHandDrawn(state);
            case CardSelected _    -> onCardSelected(state);
            case CardUnselected _  -> onCardSelected(state);
            case HandDiscarded _   -> onHandDrawn(state);
            case HandPlayed _      -> onHandPlayed(state);
            case BlindBeaten _     -> onBlindBeaten(state);
            case BlindOnGoing _    -> displayBlindInfo(state);
            case GameOver _        -> onGameOver();
            case GameWon _         -> onGameWon();
            default -> {}
        }
    }

    @Override
    public void launch(GameController controller) {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processInput(line);
            }
        }
    }

    private void processInput(String input) {
        switch(input.trim().toUpperCase()) {
            case "P" -> controller.queueAction(PlayerAction.PLAY_HAND, null);
            case "D" -> controller.queueAction(PlayerAction.DISCARD, null);
            case "Q" -> {
                controller.queueAction(PlayerAction.QUIT_GAME, null);
                running = false;
            }
            default  -> {
                try {
                    int index = Integer.parseInt(input);
                    var hand = controller.getState().getCurrentBlind().getHand().getCards();
                    if (index < 0 || index >= hand.size()) {
                        System.out.println("Entree invalide ! Tape un chiffre entre 0 et " + (hand.size() - 1) + ", P, D ou Q.");
                    } else {
                        Card card = hand.get(index);
                        controller.queueAction(PlayerAction.CARD_CHOSE, card);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entree invalide ! Tape un chiffre entre 0 et " + (controller.getState().getCurrentBlind().getHand().getMaxSize() - 1) + ", P, D ou Q.");
                }
            }
        }
    }

    private void displaySeparator() {
        System.out.println("\n-------------------------------------------------");
    }
    
    private void displayBlindInfo(GameState state) {
        System.out.println("=== " + state.getCurrentBlind().getName() + " ===");
        System.out.println("Score : " + state.getCurrentBlind().getScore() + " / " + state.getCurrentBlind().getTargetScore());
        System.out.println("Mains restantes : " + state.getCurrentBlind().getHandsCurrent());
        System.out.println("Defausses restantes : " + state.getCurrentBlind().getDiscardCurrent());
    }

    private void displayHand(GameState state) {
        System.out.println("\nTa main " + state.getCurrentBlind().getHand().getCards().size() + ":");
        for (int i = 0; i < state.getCurrentBlind().getHand().getCards().size(); i++) {
            System.out.println("[" + i + "] " + state.getCurrentBlind().getHand().getCards().get(i));
        }
    }

    private void displaySelection(GameState state) {
        System.out.println("\nCartes selectionnees : " + state.getCurrentBlind().getSelectedCards().size() + "/5");
        for (Card card : state.getCurrentBlind().getSelectedCards()) {
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
        displayGameInfo(state);
    }

    private void onHandPlayed(GameState state) {
        displaySeparator();
        displayBlindInfo(state);
    }

    private void onBlindBeaten(GameState state) {
        displaySeparator();
        System.out.println("Blind battue !");
        /*display blind selection */
    }

    private void onGameOver() {
        displaySeparator();
        System.out.println("GAME OVER");
        running = false;
    }

    private void onGameWon() {
        displaySeparator();
        System.out.println("VICTOIRE");
        running = false;
    }
}