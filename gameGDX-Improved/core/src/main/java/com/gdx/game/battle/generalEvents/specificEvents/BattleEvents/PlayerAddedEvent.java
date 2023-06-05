package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;

import static com.gdx.game.battle.BattleHUD.LOGGER;

public class PlayerAddedEvent extends BattleEvent {


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
        battleHUD.playerImage.setEntity(entity);
        battleHUD.playerImage.setCurrentAnimation(com.gdx.game.entities.Entity.AnimationType.WALK_RIGHT);
        battleHUD.playerImage.setSize(50, 50);
        battleHUD.playerImage.setPosition(0, 200);
        battleHUD.playerImage.addAction(Actions.moveTo(200, 200, 2));

        battleHUD.player.getCurrentPosition().set(((MoveToAction)battleHUD.playerImage.getActions().get(0)).getX(), battleHUD.playerImage.getY());
        LOGGER.debug("Player added on battle map");
    }
}
