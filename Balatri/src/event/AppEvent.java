package event;

public sealed interface AppEvent permits ZenEvent, GameEvent {}