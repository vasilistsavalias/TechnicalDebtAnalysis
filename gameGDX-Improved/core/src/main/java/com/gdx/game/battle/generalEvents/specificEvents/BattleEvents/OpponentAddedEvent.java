package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

 import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
 import com.gdx.game.battle.BattleHUD;
 import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
 import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
 import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
 import com.gdx.game.entities.Entity;

 import static com.gdx.game.battle.BattleHUD.LOGGER;


public class OpponentAddedEvent extends BattleEvent {


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
        battleHUD.opponentImage.setEntity(entity);
        battleHUD.opponentImage.setCurrentAnimation(com.gdx.game.entities.Entity.AnimationType.IMMOBILE);
        battleHUD.opponentImage.setSize(50,50);
        battleHUD.opponentImage.setPosition(600, 200);

        battleHUD.currentOpponentImagePosition.set(battleHUD.opponentImage.getX(),battleHUD.opponentImage.getY());
        LOGGER.debug("Opponent added on battle map");
    }
}
