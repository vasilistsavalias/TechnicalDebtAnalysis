package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;

import static com.gdx.game.battle.BattleHUD.LOGGER;

public class OpponentHitDamageEvent extends BattleEvent {

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
        int damageEnemy = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
        battleHUD.getDmgOpponentValLabel().setText(String.valueOf(damageEnemy));
        battleHUD.getDmgOpponentValLabel().setY(battleHUD.origDmgOpponentValLabelY);
//        battleShakeCam.startShaking();
        battleHUD.getDmgOpponentValLabel().setVisible(true);
        LOGGER.debug("Opponent deals " + damageEnemy + " damages");
    }
}
