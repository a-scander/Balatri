package controller;

import event.InputEvent.PlayerAction;

public record PendingAction(PlayerAction action, Object data) {}
