package event.OutputEvent;

import java.util.List;
import domain.Card;

public record HandDiscarded(List<Card> changedCards) implements HandChangeEvent {
}
