package event.OutputEvent;

import model.Phase;

public record PhaseChange(Phase phase) implements GameEvent {
    
}
