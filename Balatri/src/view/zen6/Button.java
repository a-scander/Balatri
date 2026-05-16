package view.zen6;

import java.util.function.Function;
import model.GameState;
import java.awt.Color;
import java.awt.Graphics2D;

public record Button(Function<GameState, Void> f, int x, int y, int width, int height, int zDepth) implements UIObject {
    public void callBack(GameState state){
        f.apply(state);
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
