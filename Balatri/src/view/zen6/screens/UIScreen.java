package view.zen6.screens;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import view.zen6.Point;
import view.zen6.UICard;
import view.zen6.UIHandContainer;
import view.zen6.UIObject;


public class UIScreen{
    private final List<UIObject> uiObjects = new ArrayList<>();

    public List<UIObject> getUIObjects(){return uiObjects;}

    public UIScreen(){}

    //Maybe do static classes to be able to turn UIScreen into an interface
    public void render(Graphics2D graphics){
        for(var obj : uiObjects){
            obj.draw(graphics);
        }
    }

    public UIObject getClickedObject(Point location) {
        UIObject best = null;
        int[] bestZ = { Integer.MIN_VALUE };
        for (UIObject obj : uiObjects) {
            if (!obj.contains(location)) {continue;}
    
            best = updateBest(obj, best, bestZ);
            if (obj instanceof UIHandContainer c) { // this or ugly pattern matching :/ (PM better when more UIObjects)
                for (UICard card : c.getCards()) {
                    if (card.contains(location)) {
                        best = updateBest(card, best, bestZ);
                    }
                }
            }
        }
        return best;
    }

    private UIObject updateBest(UIObject candidate, UIObject currentBest, int[] bestZ) {
        if (candidate.zDepth() > bestZ[0]) {
            bestZ[0] = candidate.zDepth();
            return candidate;
        }
        return currentBest;
    }
}
/*MainMenuScreen
BlindSelectionScreen(staic object, objects)
BlindScreen
ShopScreen
GameOverScreen */