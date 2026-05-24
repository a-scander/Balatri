
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

    public HandDescriptor currentSelectedHand;
    public UIRectangle currentScore;

    public UIRectangle remainingHands;
    public UIRectangle remainingDiscards;

    public UIRectangle moneyDisplay;

    public InfoMenu(int x, int y, int width, int height, int zDepth, GameState state) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;

        int padding = 20;

        int ColumnWidth = (width - padding * 2 - 10) / 2;

        Blind currentBlind = state.getCurrentBlind();

        if(currentBlind == null){
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Choose your\nnext Blind");
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Score: 0"); //new ScoreDisplay();
            // this.chipDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
            // this.multDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
           
            return;
        }

        int currentHeight = 80;
        int currentY = y + padding;

        this.blindDescriptor = new BlindDescriptor(currentBlind, x + padding, currentY, width - padding, currentHeight, zDepth + 1);

        currentY += currentHeight + padding * 1.5;
        currentHeight = 100;
        
        HandType handType = state.getSelectedHandType();
        String level = "";
        Score handScore = new Score(0, 0);
        if(handType != null){
            handScore = state.getModifiedHandTypeValue(handType);
            level += state.getHandLevel(handType);
        }

        this.currentSelectedHand = new HandDescriptor(x + padding, currentY, width - padding * 2, currentHeight, zDepth + 1, handType, level, handScore);

        currentY += currentHeight + padding;
        currentHeight = 40;

        this.currentScore = new UIRectangle("" + currentBlind.getScore(), x + ColumnWidth + padding * 2, currentY, ColumnWidth, currentHeight, zDepth + 1);

        this.remainingHands = new UIRectangle("Hands : " + (4 - currentBlind.getRemainingHandNb()), x + ColumnWidth + padding +  10, currentY, ColumnWidth, currentHeight, zDepth + 1);

        currentY += currentHeight + padding / 2;

        this.remainingDiscards = new UIRectangle("Discards : " + (4 - currentBlind.getRemainingDiscardNb()), x + ColumnWidth + padding + 10, currentY, ColumnWidth, currentHeight, zDepth + 1);

        currentY += currentHeight + padding;

        this.moneyDisplay = new UIRectangle("$ "/* + state.getMoney()*/, x + ColumnWidth + padding + 10, currentY, ColumnWidth, currentHeight, zDepth + 1);
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

        if(currentSelectedHand != null) {
            objects.add(currentSelectedHand);
            objects.addAll(currentSelectedHand.getObjects());
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

    public void refresh(GameState state){
        BlindChanged(state.getCurrentBlind());

        onChangedHand(state);
        moneyChanged(0);//TODO: change that 
    }

    public void BlindChanged(Blind blind){
        this.blindDescriptor.refresh(blind);
        
        this.remainingHands.setText("Hands: " + blind.getRemainingHandNb());
        this.remainingDiscards.setText("Discards: " + blind.getRemainingDiscardNb());
    }

    public void onChangedHand(GameState state){
        Blind currentBlind = state.getCurrentBlind();
        HandType handType = state.getSelectedHandType();

        Score handScore = new Score(0, 0);
        String level = "";
        if(handType != null){
            handScore = state.getModifiedHandTypeValue(handType);
            level += state.getHandLevel(handType);

        }

        currentSelectedHand.refresh(handType, level, handScore);
        
        this.currentScore.setText("Score: " + currentBlind.getScore());
    }

    public void moneyChanged(int newAmount){
        this.moneyDisplay.setText("" + newAmount);
    }

}