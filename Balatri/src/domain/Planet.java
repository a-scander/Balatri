package domain;

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
    NEPTUNE (HandType.STRAIGHT_FLUSH, 40, 4);

    private final HandType targetHand;
    private final int bonusChips;
    private final int bonusMult;

    Planet(HandType targetHand, int bonusChips, int bonusMult) {
        this.targetHand = targetHand;
        this.bonusChips = bonusChips;
        this.bonusMult  = bonusMult;
    }

    public HandType getTargetHand() { return targetHand; }
    public int getBonusChips()      { return bonusChips; }
    public int getBonusMult()       { return bonusMult;  }

    public static Planet fromHandType(HandType handType) {
        for (Planet planet : values()) {
            if (planet.getTargetHand() == handType) {
                return planet;
            }
        }
        return null; // No planet corresponds to this hand type
    }

    public static Planet getRandom() {
        Planet[] values = Planet.values();
        return values[new Random().nextInt(values.length)];
    }
}