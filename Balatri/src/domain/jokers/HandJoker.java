package domain.jokers;

import model.GameState;

public record HandJoker(JokerType type) implements GlobalJoker{
    @Override
    public void apply(GameState gameState){
        gameState.handsPerBlind += 1;
        if(gameState.getCurrentBlind() != null){
            gameState.getCurrentBlind().handsPerBlind += 1;
        }
    }

    @Override
    public void destroy(GameState gameState){
        gameState.handsPerBlind -= 1;
        if(gameState.getCurrentBlind() != null){
            gameState.getCurrentBlind().handsPerBlind -= 1;
        }
    }
}
