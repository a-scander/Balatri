
package view.zen6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import domain.Blind;
import domain.HandType;
import domain.Score;
import model.GameState;

public final class InfoMenu implements UIObject {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int zDepth;

    public BlindDescriptor blindDescriptor;

    public UIRectangle currentScore;

    public UIRectangle chipDisplay;
    public UIRectangle multDisplay;

    public UIRectangle remainingHands;
    public UIRectangle remainingDiscards;

    public UIRectangle moneyDisplay;

    public InfoMenu(
            int x,
            int y,
            int width,
            int height,
            int zDepth,
            GameState state
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;

        Blind currentBlind = state.getCurrentBlind();

        if(currentBlind == null){
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Choose your\nnext Blind");
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Score: 0"); //new ScoreDisplay();
            // this.chipDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
            // this.multDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
           
            return;
        }

        int padding = 20;

        int leftColumnWidth = width / 2 - padding;
        int rightColumnWidth = width / 2 - padding;

        this.blindDescriptor = new BlindDescriptor(currentBlind, x + padding, y + padding, leftColumnWidth, 140, zDepth + 1);

        this.currentScore = new UIRectangle("Score : " + currentBlind.getScore(), x + padding, y + 180, leftColumnWidth, 60, zDepth + 1);

        HandType handType = HandType.HIGH_CARD;
        Score score = state.getModifiedHandTypeValue(handType);

        int rightX = x + width / 2;

        this.chipDisplay = new UIRectangle("Chips : " + score.chips(), rightX, y + padding, rightColumnWidth, 60, zDepth + 1);

        this.multDisplay = new UIRectangle("Mult : " + score.mult(), rightX, y + 90, rightColumnWidth, 60, zDepth + 1);

        this.remainingHands = new UIRectangle("Hands : " + (4 - currentBlind.getHandsCurrent()), 
                rightX,
                y + 180,
                rightColumnWidth,
                50,
                zDepth + 1
                
        );

        this.remainingDiscards = new UIRectangle("Discards : " + (4 - currentBlind.getDiscardCurrent()), 
                rightX,
                y + 250,
                rightColumnWidth,
                50,
                zDepth + 1
                
        );

        this.moneyDisplay = new UIRectangle("$ "/* + state.getMoney()*/, x + padding,
                y + height - 70,
                width - padding * 2,
                50,
                zDepth + 1
        );
    }

    @Override
    public int zDepth() {
        return zDepth;
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.GRAY);
        graphics.fillRect(x, y, width, height);
        
        // Draw button border
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, height);
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    public List<UIObject> getObjects() {
        List<UIObject> objects = new ArrayList<>();

        if(blindDescriptor != null) {
            objects.add(blindDescriptor);
            objects.addAll(blindDescriptor.getObjects());
        }

        if(currentScore != null) {
            objects.add(currentScore);
        }

        if(chipDisplay != null) {
            objects.add(chipDisplay);
        }

        if(multDisplay != null) {
            objects.add(multDisplay);
        }

        if(remainingHands != null) {
            objects.add(remainingHands);
        }

        if(remainingDiscards != null) {
            objects.add(remainingDiscards);
        }

        if(moneyDisplay != null) {
            objects.add(moneyDisplay);
        }

        return objects;
    }
}