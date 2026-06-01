package domain.jokers;

import model.GameState;

public interface GlobalJoker extends Joker{
    public void destroy(GameState gameState);
}
