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

        // Draw card background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(x, y, width, height);

        // Draw card border (highlight if selected)
        graphics.setColor(isSelected ? Color.RED : Color.BLACK);
        graphics.drawRect(x, y, width, height);

        // Draw card details (rank and suit)
        graphics.setColor(Color.BLACK);
        String cardText = card.toStringSmall();
        graphics.drawString(cardText, x + 5, y + height / 2);
    }
}
