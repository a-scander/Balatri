package view.zen6;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import domain.Blind;
import model.GameState;

public final class InfoMenu implements UIObject{
    public int x;
    public int y;
    public int width;
    public int height;
    public int zDepth;

    /*
    * BlindDescriptor of currentBlind
    * CurrentScore
    * CurrentHandType -> HandChangeEvent calls a refresh on this :/ 
    * CurrentValue of handType -> chipDisplay, multDisplay
    * remaining hands
    * remaining discards
    * money
    * ante / remaining antes
    * round (currentBlindIndex)
    * 
    * 
    * 
    */
    public InfoMenu(int x, int y, int width, int height, int zDepth, GameState state){
        Blind currentBlind = state.getCurrentBlind();
        if(currentBlind == null){
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Choose your\nnext Blind");
            // this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Score: 0"); //new ScoreDisplay();
            // this.chipDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
            // this.multDisplay =  new UIRectangle(, , , , , zDepth + 1, 0);
           
            return;
        }
        /*this.BlindDescriptor = new BlindDescriptor(currentBlind, newX, newY, newWidth, newHeight, zDepth + 1); 
        * this.currentScore = new UIRectangle(, , , , , zDepth + 1, "Score: " + currentBlind.getScore().toString());

        * HandType = state.getSelectedCardHanType();
        * Score score =  state.getModifiedHandTypeValue(); // -> declare a class score(chips, mult)
        * this.chipDisplay =  new UIRectangle(, , , , , zDepth + 1, score.chips());
        * this.multDisplay =  new UIRectangle(, , , , , zDepth + 1, score.mult());

        * this.remainingHands =  new UIRectangle(, , , , , zDepth + 1, currentBlind.getRemainingHands().toString());
        * this.remainingHands =  new UIRectangle(, , , , , zDepth + 1, currentBlind.getRemainingDiscards().toString());
        
        * this.remainingHands =  new UIRectangle(, , , , , zDepth + 1, state.getMoney().toString());

        * 
        * 
        * */
        
    }

    public int zDepth(){return this.zDepth;}

    @Override
    public void draw(Graphics2D graphics){

    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
}
