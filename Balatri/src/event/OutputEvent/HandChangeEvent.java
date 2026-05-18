package event.OutputEvent;

import java.util.List;

import domain.Card;

public sealed interface HandChangeEvent extends GameEvent permits HandDrawn, HandDiscarded, CardUnselected, CardSelected {
    public List<Card> changedCards();
}

