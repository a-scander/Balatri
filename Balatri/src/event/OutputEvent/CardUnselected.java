package event.OutputEvent;

import java.util.List;
import domain.Card;

public record CardUnselected(List<Card> changedCards) implements HandChangeEvent {}
