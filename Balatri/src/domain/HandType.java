package domain;

public enum HandType {
    HIGH_CARD(5, 1),
    PAIR(10, 2),
    TWO_PAIR(20, 2),
    THREE_OF_A_KIND(30, 3),
    STRAIGHT(30, 4),
    FLUSH(35, 4),
    FULL_HOUSE(40, 4),
    FOUR_OF_A_KIND(60, 7),
    ROYAL_FLUSH(80, 8),
    STRAIGHT_FLUSH(100, 8),
    FIVE_OF_A_KIND(120, 12);

    private final Score score;

    HandType(int baseChips, int baseMult) {
        this.score = new Score(baseChips, baseMult);
    }

    public Score getScore(){ return this.score;}
}