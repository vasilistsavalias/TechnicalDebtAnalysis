package com.gdx.game.events.SlotEvents;

import com.gdx.game.events.Event;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.inventory.slot.InventorySlot;
import com.gdx.game.inventory.store.StoreInventoryUI;

public abstract class SlotEvent implements Event {
    public abstract void PerformInventoryUIAction(InventorySlot slot, InventoryUI inventoryUI);
    public abstract void PerformStoreInventoryUIAction(InventorySlot slot, StoreInventoryUI storeInventoryUI);
}
