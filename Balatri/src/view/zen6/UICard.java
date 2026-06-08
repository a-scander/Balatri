package view.zen6;

import domain.Card;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public record UICard(Card card, int x, int y, int width, int height, int zDepth, boolean isSelected) implements UIObject {
    private static final BufferedImage SPRITE_SHEET = loadSpriteSheet();
    private static final int SPRITE_WIDTH = 71;
    private static final int SPRITE_HEIGHT = 95;

    public UICard {
        if (card == null) {
            throw new NullPointerException("card cannot be null");
        }
    }

    private static BufferedImage loadSpriteSheet() {
        try {
            var stream = UICard.class.getResourceAsStream("/data/PC _ Computer - Balatro - Playing Cards - Playing Cards.png");
            if (stream == null) {
                stream = UICard.class.getResourceAsStream("/data/heart two.png");
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Unable to load card sprite sheet: " + e.getMessage());
        }
    }
    
    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }
    
    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(245, 245, 245));
        graphics.fillRect(x, y, width, height);

        if (isSelected) {
            graphics.setColor(new Color(232, 182, 73));
            graphics.setStroke(new java.awt.BasicStroke(3));
        } else {
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new java.awt.BasicStroke(1));
        }
        graphics.drawRect(x, y, width, height);
        graphics.setColor(Color.BLACK);

        if (SPRITE_SHEET != null) {
            int sx1 = getRankX();
            int sy1 = getSuitY();
            int sx2 = sx1 + SPRITE_WIDTH;
            int sy2 = sy1 + SPRITE_HEIGHT;
            graphics.drawImage(SPRITE_SHEET, x, y, x + width, y + height, sx1, sy1, sx2, sy2, null);
        }
        /* deprecated: old card text display before sprite
        String cardText = card.toStringSmall();
        graphics.drawString(cardText, x + 5, y + height / 2);*/
    }

    public int getSuitY() {
        return switch (this.card.suit()) {
            case HEARTS -> 0;
            case CLUBS -> SPRITE_HEIGHT;
            case DIAMONDS -> SPRITE_HEIGHT * 2;
            case SPADES -> SPRITE_HEIGHT * 3;
        };
    }

    public int getRankX() {
        return switch (this.card.rank()) {
            case TWO -> 0;
            case THREE -> SPRITE_WIDTH * 1;
            case FOUR -> SPRITE_WIDTH * 2;
            case FIVE -> SPRITE_WIDTH * 3;
            case SIX -> SPRITE_WIDTH * 4;
            case SEVEN -> SPRITE_WIDTH * 5;
            case EIGHT -> SPRITE_WIDTH * 6;
            case NINE -> SPRITE_WIDTH * 7;
            case TEN -> SPRITE_WIDTH * 8;
            case JACK -> SPRITE_WIDTH * 9;
            case QUEEN -> SPRITE_WIDTH * 10;
            case KING -> SPRITE_WIDTH * 11;
            case ACE -> SPRITE_WIDTH * 12;
        };
    }
}
