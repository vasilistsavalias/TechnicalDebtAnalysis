package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;
import com.gdx.game.screen.GameScreen;

import static com.gdx.game.battle.BattleHUD.LOGGER;

public class OpponentTurnDoneEvent extends BattleEvent {


    @Override
    public void onNotify(Entity enemyEntity, BattleEvent event) {

    }


    public void onNotify(String value, ComponentEvent event) {

    }


    public void onNotify(String value, InventoryEvent event) {

    }


    public void onNotify(int value, StatusEvent event) {

    }

    @Override
    public void activateEvent(Entity entity, BattleHUD battleHUD) {
        if(GameScreen.getGameState() != GameScreen.GameState.GAME_OVER) {
            battleHUD.battleUI.setVisible(true);
            battleHUD.battleUI.setTouchable(Touchable.enabled);
        }
        LOGGER.debug("Opponent turn done");
    }
    }

