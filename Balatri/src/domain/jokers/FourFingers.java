package domain.jokers;

import domain.configs.StraightConfig;
import model.GameState;

public record FourFingers(JokerType type) implements GlobalJoker {
    @Override
    public void apply(GameState gameState) {
        gameState.straightConfig = new StraightConfig(4, gameState.straightConfig.allowedGaps());
    }

    @Override
    public void destroy(GameState gameState) {
        gameState.straightConfig = new StraightConfig(5, gameState.straightConfig.allowedGaps());
    }
}