package view.zen6;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import controller.GameController;

//TODO: Make button take the queueAction function instead of the full Consumer<controller>, check for use case for at commit b6c24df6951019597ae456d54c26453247623c5c every button is convertible
public record Button(Consumer<GameController> action, String name, int x, int y, int width, int height, int zDepth) implements UIObject {
    public void onClick(GameController controller){
        action.accept(controller);
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        // Balatro style: dark purple background with gold border
        graphics.setColor(new Color(136, 68, 170)); // Purple (#8844aa)
        graphics.fillRect(x, y, width, height);
        
        // Draw button border (gold)
        graphics.setColor(new Color(232, 182, 73)); // Gold (#e8b649)
        graphics.setStroke(new java.awt.BasicStroke(2));
        graphics.drawRect(x, y, width, height);

        FontMetrics fm = graphics.getFontMetrics();

        int textWidth = fm.stringWidth(name);
        int textHeight = fm.getHeight();

        int textX = x + (width - textWidth) / 2;
        int textY = y + ((height - textHeight) / 2) + fm.getAscent();

        // White text
        graphics.setColor(Color.WHITE);
        graphics.drawString(name, textX, textY);
    }
}
