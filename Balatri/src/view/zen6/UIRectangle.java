package view.zen6;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public final class UIRectangle implements UIObject {
    public String text;
    public int x;
    public int y;
    public int width;
    public int height;
    public int zDepth;

    public UIRectangle(String text,  int x, int y, int width, int height, int zDepth){
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;
    }

    public String text(){return this.text;}
    public int x(){return x;}
    public int y(){return y;}
    public int width(){return width;}
    public int height(){return height;}
    public int zDepth(){return zDepth;}

    public void setText(String text){this.text = text;}


    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        // Draw background (dark purple)
        graphics.setColor(new Color(26, 31, 58)); // Dark purple background (#1a1f3a)
        graphics.fillRect(x, y, width, height);
        
        // Draw border (gold)
        graphics.setColor(new Color(232, 182, 73)); // Gold (#e8b649)
        graphics.setStroke(new java.awt.BasicStroke(2));
        graphics.drawRect(x, y, width, height);

        if(this.text == null){return;}
        String blindText = this.text;
        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(blindText);
        int textHeight = fm.getHeight();
        
        // White text
        graphics.setColor(Color.WHITE);
        graphics.drawString(blindText, x + (width - textWidth) / 2, y + (height + textHeight / 2) / 2);
    }
}
