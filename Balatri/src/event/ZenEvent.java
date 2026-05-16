package event;

import com.github.forax.zen.Event;

public record ZenEvent(Event event) implements AppEvent {}