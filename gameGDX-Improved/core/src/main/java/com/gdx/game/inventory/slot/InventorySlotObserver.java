package com.gdx.game.inventory.slot;

import com.gdx.game.events.SlotEvents.AddedItemEvent;
import com.gdx.game.events.SlotEvents.RemovedItemEvent;
import com.gdx.game.events.SlotEvents.SlotEvent;

public interface InventorySlotObserver {
    SlotEvent ADDED_ITEM =  new AddedItemEvent();
    SlotEvent REMOVED_ITEM = new RemovedItemEvent();

    void onNotify(final InventorySlot slot, SlotEvent event);
}
