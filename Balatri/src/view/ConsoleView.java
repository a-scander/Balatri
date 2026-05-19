package view;

import domain.Card;
import event.InputEvent.PlayerAction;
import event.OutputEvent.*;
import model.GameState;
import model.Phase;

import java.util.Scanner;
import controller.GameController;

public final class ConsoleView implements View {

    private final GameController controller;
    private volatile boolean running = true;

    public ConsoleView(GameController controller) {
        this.controller = controller;
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

    // Handles incoming game engine events and routes them to specific UI display methods.
    // Overly simplified for now, but can be expanded to handle more complex UI updates as needed.
    @Override
    public void onEvent(GameEvent event) {
        switch(event) {
            case HandDrawn _       -> onHandDrawn();
            case CardSelected cs    -> {if(cs.changedCards() == null){IO.println("Maximum selection size reached");}onCardSelected();}
            case CardUnselected _  -> onCardSelected();
            case HandDiscarded _   -> onHandDrawn();
            case HandPlayed hp      -> onHandPlayed(hp);
            case BlindBeaten _     -> onBlindBeaten();
            case BlindOnGoing _    -> {}
            case GameOver _        -> onGameOver();
            case GameWon _         -> onGameWon();
            case PhaseChange pc    -> processPhaseChange(pc.phase());
        }
    }

    private void processInput(String input) {
        switch(input.trim().toUpperCase()) { //Needs to check for current phase to accept input :/
            case "M" -> controller.queueAction(PlayerAction.START_GAME, null);
            case "L" -> controller.queueAction(PlayerAction.SELECT_BLIND, null);
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
                        IO.println("Entree invalide ! Tape un chiffre entre 0 et " + (hand.size() - 1) + ", P, D ou Q.");
                    } else {
                        Card card = hand.get(index);
                        controller.queueAction(PlayerAction.CARD_CHOSE, card);
                    }
                } catch (NumberFormatException e) {
                    IO.println("Entree invalide ! Tape un chiffre entre 0 et " + (controller.getState().getCurrentBlind().getHand().getMaxSize() - 1) + ", P, D ou Q.");
                }
            }
        }
    }

    private void processPhaseChange(Phase phase){
        switch(phase){
            case MAIN_SCREEN -> displayMainScreen();
            case BLIND_SELECTION -> displayBlindSelection();
            case IN_BLIND -> displayGameInfo();
            case GAME_OVER, INITIALIZE, IN_SHOP -> {}
            default -> {}
        }
    }

    private void displaySeparator() {
        IO.println("\n-------------------------------------------------");
    }
    
    private void displayBlindInfo() {
        GameState state = controller.getState();
        IO.println("=== " + state.getCurrentBlind().getName() + " ===");
        IO.println("Score : " + state.getCurrentBlind().getScore() + " / " + state.getCurrentBlind().getTargetScore());
        IO.println("Mains restantes : " + state.getCurrentBlind().getHandsCurrent());
        IO.println("Defausses restantes : " + state.getCurrentBlind().getDiscardCurrent());
    }

    private void displayHand() {
        GameState state = controller.getState();
        IO.println("\nTa main " + state.getCurrentBlind().getHand().getCards().size() + ":");
        for (int i = 0; i < state.getCurrentBlind().getHand().getCards().size(); i++) {
            IO.println("[" + i + "] " + state.getCurrentBlind().getHand().getCards().get(i));
        }
    }

    private void displaySelection() {
        GameState state = controller.getState();
        IO.println("\nCartes selectionnees : " + state.getCurrentBlind().getSelectedCards().size() + "/5");
        for (Card card : state.getCurrentBlind().getSelectedCards()) {
            IO.println("- " + card);
        }
    }

    private void displayGameInfo() {
        displaySeparator();
        displayBlindInfo();
        displayHand();
        displaySelection();
    }

    private void onHandDrawn() {
        displayGameInfo();
    }

    private void onCardSelected() {
        displayGameInfo();
    }

    private void onHandPlayed(HandPlayed hp) {
        displaySeparator();
        IO.println("score: " + hp.score() + ", played cards: " + hp.discardedCards() + ", redrawn cards" + hp.drawnCards());
    }

    private void onBlindBeaten() {
        displaySeparator();
        IO.println("Blind battue !");
        /*display blind selection */
    }

    private void onGameOver() {
        displaySeparator();
        IO.println("GAME OVER");
        running = false;
    }

    private void onGameWon() {
        displaySeparator();
        IO.println("VICTOIRE");
        running = false;
    }

    private void displayMainScreen() {
        IO.println("Press <M> to start blind selection");
    }

    private void displayBlindSelection(){
        IO.println("Press <L> to start blind");
    }
}