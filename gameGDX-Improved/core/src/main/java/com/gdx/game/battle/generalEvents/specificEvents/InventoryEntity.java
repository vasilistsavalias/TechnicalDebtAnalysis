package com.gdx.game.battle.generalEvents.specificEvents;

import com.gdx.game.battle.generalEvents.GeneralEntity;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.*;

public interface InventoryEntity extends GeneralEntity {
    InventoryEvent updatedDP = new UpdatedDPEvent();
    InventoryEvent itemConsumed = new ItemConsumedEvent();
    InventoryEvent addWandUp = new AddWandUpEvent();
    InventoryEvent removeWandAp = new RemoveWandApEvent();
    InventoryEvent refreshStats = new RefreshStatsEvent();
    InventoryEvent NoneInv = new NoneInvEvent();

    public void onNotify(String value, InventoryEvent event);
}
