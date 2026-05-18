package view.zen6;

import java.awt.Color;
import java.awt.Graphics2D;
import model.GameState;

import controller.GameController;

public record Button(java.util.function.BiConsumer<GameController, GameState> action, int x, int y, int width, int height, int zDepth) implements UIObject {
    public void onClick(GameController controller, GameState state){
        action.accept(controller, state);
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        // Draw button background
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, width, height);
        
        // Draw button border
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, height);
    }
}
