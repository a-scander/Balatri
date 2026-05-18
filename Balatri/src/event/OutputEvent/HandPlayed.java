package event.OutputEvent;

import java.util.List;

import domain.Card;
import domain.HandType;

public record HandPlayed(int score, HandType handType, List<Card> discardedCards, List<Card> drawnCards) implements GameEvent {}