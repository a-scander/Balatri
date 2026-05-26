package domain;

import java.util.List;

public record HandResult(HandType type, List<Card> scoringCards) {}