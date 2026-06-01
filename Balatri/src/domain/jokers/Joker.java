package domain.jokers;

import model.GameState;

public interface Joker {
    public JokerType type();
    public void apply(GameState gameState);
}
