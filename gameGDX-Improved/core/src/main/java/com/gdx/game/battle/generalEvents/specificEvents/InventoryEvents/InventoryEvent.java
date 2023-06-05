package com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEntity;

public abstract class InventoryEvent implements InventoryEntity {
    public abstract void activateEvent(String value, BattleHUD inventoryEvent);

}
