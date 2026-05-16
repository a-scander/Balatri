package view.zen6;

import java.awt.Graphics2D;

public sealed interface UIObject permits UICard, Button, UIRectangle {
    Bounds getBounds();
    
    int zDepth();
    
    default boolean contains(Point point) {
        return getBounds().contains(point);
    }

    public void draw(Graphics2D graphics);
}
