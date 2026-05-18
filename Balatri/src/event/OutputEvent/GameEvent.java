package event.OutputEvent;

public sealed interface GameEvent permits HandChangeEvent, HandPlayed, BlindBeaten, BlindOnGoing, GameWon, GameOver /*PhaseChange permits BlindBeaten, BlindOnGoing, GameWon, GameOver */ {}