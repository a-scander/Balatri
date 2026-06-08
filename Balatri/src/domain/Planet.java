package domain;

import java.nio.file.FileVisitOption;
import java.util.Random;

public enum Planet {
    PLUTON  (HandType.HIGH_CARD,      10, 1),
    MERCURE (HandType.PAIR,           15, 1),
    URANUS  (HandType.TWO_PAIR,       20, 1),
    VENUS   (HandType.THREE_OF_A_KIND,20, 2),
    SATURNE (HandType.STRAIGHT,       30, 3),
    JUPITER (HandType.FLUSH,          15, 2),
    TERRE   (HandType.FULL_HOUSE,     25, 2),
    MARS    (HandType.FOUR_OF_A_KIND, 30, 3),
    NEPTUNE (HandType.STRAIGHT_FLUSH, 40, 4),
    SECRET1 (HandType.FIVE_OF_A_KIND, 0, 0),
    SECRET2 (HandType.ROYAL_FLUSH, 0, 0);
    /* Easy to add the hidden planets ;P */

    private final HandType targetHand;
    private final Score score;

    Planet(HandType targetHand, int bonusChips, int bonusMult) {
        this.targetHand = targetHand;
        this.score = new Score(bonusChips, bonusMult);
    }

    public HandType getTargetHand() { return targetHand; }
    public Score getScore() { return score;}

    public static Planet fromHandType(HandType handType) {
        if(handType == HandType.FIVE_OF_A_KIND){
            return PLUTON;
        }
        if(handType == HandType.ROYAL_FLUSH){
            return SATURNE;
        } 
        for (Planet planet : values()) {
            if (planet.getTargetHand() == handType) {
                return planet;
            }
        }
        return null; // No planet corresponds to this hand type
    }

    public static Planet getRandom() {
        Planet[] values = Planet.values();
        Random r = new Random();
        Planet result = values[r.nextInt(values.length)];
        while(result.targetHand == HandType.FIVE_OF_A_KIND){
            result = values[r.nextInt(values.length)];
        }
        if(result == SECRET1){
            return SATURNE;
        }
        return result;
    }
}