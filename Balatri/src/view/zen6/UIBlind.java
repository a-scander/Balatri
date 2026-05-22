package view.zen6;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import domain.Blind;


public final class UIBlind implements UIObject {
    public Blind blind;
    public Button selectButton;
    /* Button skipBlindButton */
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int zDepth;

    public UIBlind(int x, int y, int width, int height, int zDepth, Blind blind, boolean isCurrent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;
        this.blind = blind;
        this.selectButton = isCurrent ? new Button((ctrl) -> ctrl.queueAction(event.InputEvent.PlayerAction.SELECT_BLIND, blind), 
                                                    "Current Blind", x + 10, y + height / 2, width - 20, height / 4, zDepth + 1)
                                        : new Button((ctrl) -> {}, 
                                                "Upcoming", x + width / 4, y + height / 2, width / 2, height / 4, zDepth + 1);


    }
    @Override
    public int zDepth() {
        return zDepth;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D graphics) {//TODO: make this look better
        // Draw a semi-transparent overlay to represent the blind
        graphics.setColor(new Color(0, 0, 0, 128)); // Semi-transparent black
        graphics.fillRect(x, y, width, height);
        
        // Optionally, draw text or an icon to indicate it's a blind
        graphics.setColor(Color.WHITE);
        String blindText = this.blind.getName();
        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(blindText);
        int textHeight = fm.getHeight();
        graphics.drawString(blindText, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - fm.getDescent() - 30);
    }
    
}
