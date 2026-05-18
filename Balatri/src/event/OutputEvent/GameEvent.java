package event.OutputEvent;

public sealed interface GameEvent permits HandChangeEvent, HandPlayed, BlindBeaten, GameWon, GameOver /*PhaseChange permits BlindBeaten, GameWon, GameOver */ {}