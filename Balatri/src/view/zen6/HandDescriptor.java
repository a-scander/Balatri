package view.zen6;

import java.awt.Graphics2D;
import java.util.List;

import domain.HandType;
import domain.Score;

public final class HandDescriptor implements UIObject {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int zDepth;
    
    public UIRectangle handTypeDisplay;
    public UIRectangle chipDisplay;
    public UIRectangle multDisplay;

    public HandDescriptor(int x, int y, int width, int height, int zDepth, HandType handType, String HandLevel, Score handScore) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;
        
        int padding = 10;
        int rowHeight = (height - padding) / 2;
        int colWidth = (width - padding * 3) / 2;

        this.handTypeDisplay = new UIRectangle("<HandType> Lvl: " + HandLevel, x + padding, y, width - padding * 2, rowHeight, zDepth + 1);

        this.chipDisplay = new UIRectangle("Chips : " + handScore.chips(), x + padding, y + rowHeight + padding, colWidth, rowHeight, zDepth + 1);

        this.multDisplay = new UIRectangle("Mult : " + handScore.mult(), x + colWidth + padding * 2, y + rowHeight + padding, colWidth, rowHeight, zDepth + 1);

        //add the handtype name
    }

    @Override
    public void draw(Graphics2D graphics) {
        handTypeDisplay.draw(graphics);
        chipDisplay.draw(graphics);
        multDisplay.draw(graphics);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    @Override
    public int zDepth() {
        return zDepth;
    }

    public List<UIObject> getObjects() {
        return List.of(handTypeDisplay, chipDisplay, multDisplay);
    }

    public void refresh(HandType handType, String handLevel, Score handScore){
        handTypeDisplay.setText("<HandType> Lvl: " + handLevel);
        chipDisplay.setText("Chips: " + handScore.chips());
        multDisplay.setText("Mult: " + handScore.mult());
    }
}