package view.zen6;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public record UIRectangle(String text,  int x, int y, int width, int height, int zDepth, String name) implements UIObject {
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

        if(this.name == null){return;}
        String blindText = this.name;
        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(blindText);
        int textHeight = fm.getHeight();
        graphics.drawString(blindText, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - fm.getDescent() - 30);
    }
}
