package view.zen6;

import domain.Card;
import java.awt.Color;
import java.awt.Graphics2D;

public record UICard(Card card, int x, int y, int width, int height, int zDepth, boolean isSelected) implements UIObject {
    public Card getCard() {
        return card;
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
    
    @Override
    public void draw(Graphics2D graphics) {

        // Draw card background (light)
        graphics.setColor(new Color(245, 245, 245)); // Light card background
        graphics.fillRect(x, y, width, height);

        // Draw card border (gold if selected, black otherwise)
        if (isSelected) {
            graphics.setColor(new Color(232, 182, 73)); // Gold (#e8b649)
            graphics.setStroke(new java.awt.BasicStroke(3));
        } else {
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new java.awt.BasicStroke(1));
        }
        graphics.drawRect(x, y, width, height);

        // Draw card details (rank and suit)
        graphics.setColor(Color.BLACK);
        String cardText = card.toStringSmall();
        graphics.drawString(cardText, x + 5, y + height / 2);
    }
}
