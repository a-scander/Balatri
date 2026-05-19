package view.zen6;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.function.BiConsumer;

import model.GameState;

import controller.GameController;

//The comsumer might be over kill in terms of parameters
public record Button(BiConsumer<GameController, GameState> action, String name, int x, int y, int width, int height, int zDepth) implements UIObject {
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

        FontMetrics fm = graphics.getFontMetrics();

        int textWidth = fm.stringWidth(name);
        int textHeight = fm.getHeight();

        int textX = x + (width - textWidth) / 2;
        int textY = y + ((height - textHeight) / 2) + fm.getAscent();

        graphics.drawString(name, textX, textY);
    }
}
