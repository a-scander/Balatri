package event.InputEvent;

// Might do an interface with classes for better extensibility and readability in GameState instead of Object type casting
public enum PlayerAction {
    CARD_CHOSE,
    PLAY_HAND,
    DISCARD,
    QUIT_GAME,
    START_GAME,
    SELECT_BLIND,
    BLIND_SELECTION
}