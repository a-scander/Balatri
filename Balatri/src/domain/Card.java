package domain;

import java.util.Objects;

public record Card(Rank rank, Suit suit) {
    public Card{
        Objects.requireNonNull(rank);
        Objects.requireNonNull(suit);
    }

    public boolean isAce(){
        return rank == Rank.ACE;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public String toStringSmall() {
        return "" + rank.getValue() + suit.name().charAt(0);
    }
}