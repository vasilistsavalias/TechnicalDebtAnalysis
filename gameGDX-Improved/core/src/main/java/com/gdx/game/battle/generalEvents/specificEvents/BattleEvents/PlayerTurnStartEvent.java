package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;


public class PlayerTurnStartEvent extends BattleEvent {




    @Override
    public void activateEvent(Entity entity, BattleHUD battleHUD) {
        BattleHUD.LOGGER.debug("Player turn start");
        battleHUD.getBattleUI().setVisible(false);
        battleHUD.getBattleStatusUI().setTouchable(Touchable.disabled);
    }
}
