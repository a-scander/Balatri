package view;

import domain.Card;
import event.GameEvent;
import event.PlayerAction;
import model.GameState;
import view.ui.CardUI;
import java.util.ArrayList;
import java.util.List;

import controller.GameController;

public class Zen6View implements View {

    private GameController  controller;
    private List<CardUI> cardUIs = new ArrayList<>();

    public Zen6View(GameController controller){
        this.controller=controller;
    }
    
    @Override
    public void onEvent(GameEvent event, GameState state) {
        switch(event) {

        }
    }

}