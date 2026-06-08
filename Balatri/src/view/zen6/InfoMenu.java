
package view.zen6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import domain.Blind;
import domain.HandResult;
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
        int innerPadding = 10;
        int ColumnWidth = (width - padding * 2 - innerPadding) / 2;

        Blind currentBlind = state.getCurrentBlind();
        int currentY = y + padding;
        int currentHeight = 80;
        this.blindDescriptor = new BlindDescriptor(currentBlind, x + padding, currentY, width - padding, currentHeight, zDepth + 1);

        currentY += currentHeight + padding + innerPadding;
        currentHeight = 100;
        
        HandResult result = (currentBlind == null) ? null: state.getSelectedHandType();
        HandType handType = result == null ? null : result.type();
        
        int level = 0;
        Score handScore = new Score(0, 0);
        if(handType != null){
            handScore = state.getModifiedHandTypeValue(handType);
            level = state.getHandLevel(handType);
        }

        this.currentSelectedHand = new HandDescriptor(x + padding, currentY, width - padding * 2, currentHeight, zDepth + 1, handType, level, handScore);
        currentY += currentHeight + padding / 2;
        currentHeight = 30;

        this.currentScore = new UIRectangle("Score: " + ((currentBlind == null) ? "":currentBlind.getScore()), (int)(x + ColumnWidth + padding + innerPadding), currentY, ColumnWidth, currentHeight, zDepth + 1);
        currentY += currentHeight + innerPadding;
        currentHeight = 30;

        this.remainingHands = new UIRectangle("Hands : " + ((currentBlind == null) ? "":(4 - currentBlind.getRemainingHandNb())), (int)(x + ColumnWidth + padding + innerPadding), currentY, ColumnWidth, currentHeight, zDepth + 1);
        currentY += currentHeight + innerPadding;

        this.remainingDiscards = new UIRectangle("Discards : " + ((currentBlind == null) ? "":(4 - currentBlind.getRemainingDiscardNb())), x + ColumnWidth + padding + innerPadding, currentY, ColumnWidth, currentHeight, zDepth + 1);
        currentY += currentHeight + padding;

        this.moneyDisplay = new UIRectangle("$ "/* + state.getMoney()*/, x + ColumnWidth + padding + 10, currentY, ColumnWidth, currentHeight, zDepth + 1);
    }

    @Override
    public int zDepth() {
        return zDepth;
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

        if(currentScore != null){
            objects.add(currentScore);
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

    public void reset(){
        this.currentScore.text = "Score: 0";
        this.remainingHands.text = "Hands:";
        this.remainingDiscards.text = "Discards:";
        this.currentSelectedHand.reset();
    }

    public void BlindChanged(Blind blind){
        this.blindDescriptor.refresh(blind);
        
        this.remainingHands.setText("Hands: " + blind.getRemainingHandNb());
        this.remainingDiscards.setText("Discards: " + blind.getRemainingDiscardNb());
    }

    public void onChangedHand(GameState state){
        Blind currentBlind = state.getCurrentBlind();
        HandResult result = state.getSelectedHandType();
        HandType handType = result != null ? result.type() : null;
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