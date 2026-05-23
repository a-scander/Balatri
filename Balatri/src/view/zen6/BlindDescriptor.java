package view.zen6;

import java.awt.Graphics2D;
import java.util.List;

import domain.Blind;

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
        int rowHeight = (height - padding) / 2;
        int colWidth = (width - padding * 3) / 2;

        this.blindName = new UIRectangle(blind.getName(), x, y, width - padding * 2, rowHeight, zDepth + 1);

        this.blindType = new UIRectangle("Type : ?To add", x, y + padding + rowHeight, colWidth, rowHeight, zDepth + 1);

        this.targetScore = new UIRectangle("Target : " + blind.getTargetScore(), x + padding + colWidth, y + padding + rowHeight, colWidth, rowHeight, zDepth + 1);
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

    public void refresh(Blind blind){
        blindName.setText(blind.getName());
        blindType.setText("Type : ?To add");
        targetScore.setText("Target : " + blind.getTargetScore());
    }
}