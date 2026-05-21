package event.OutputEvent;

public sealed interface GameEvent permits HandChangeEvent, PhaseChange, BlindBeaten, BlindOnGoing, GameWon, GameOver, GameClosed /*PhaseChange permits BlindBeaten, BlindOnGoing, GameWon, GameOver */ {}