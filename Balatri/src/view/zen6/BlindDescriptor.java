package view.zen6;

import java.awt.Graphics2D;
import java.util.List;

import domain.Blind;
import domain.BlindType;

public final class BlindDescriptor implements UIObject {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final int zDepth;

    private final UIRectangle blindName;
    private final UIRectangle blindType;
    private final UIRectangle targetScore;

    public BlindDescriptor(Blind blind, int x, int y, int width, int height, int zDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zDepth = zDepth;

        int padding = 10;
        int rowHeight = (height - padding * 4) / 3;

        this.blindName = new UIRectangle(blind.getName(), x + padding, y + padding, width - padding * 2, rowHeight, zDepth + 1);

        this.blindType = new UIRectangle("Type : ?To add", x + padding, y + padding * 2 + rowHeight, width - padding * 2, rowHeight, zDepth + 1);

        this.targetScore = new UIRectangle("Target : " + blind.getTargetScore(), x + padding, y + padding * 3 + rowHeight * 2, width - padding * 2, rowHeight, zDepth + 1);
    }

    @Override
    public void draw(Graphics2D graphics) {
        blindName.draw(graphics);
        blindType.draw(graphics);
        targetScore.draw(graphics);
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
        return List.of(blindName, blindType, targetScore);
    }
}