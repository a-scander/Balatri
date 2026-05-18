package event.OutputEvent;

import java.util.List;
import domain.Card;

public record HandDrawn(List<Card> changedCards) implements HandChangeEvent {
}
