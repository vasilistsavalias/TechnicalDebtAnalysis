package com.gdx.game.inventory.slot;

import com.gdx.game.events.SlotEvents.SlotEvent;

public interface InventorySlotSubject {

    void addObserver(InventorySlotObserver inventorySlotObserver);
    void removeObserver(InventorySlotObserver inventorySlotObserver);
    void removeAllObservers();
    void notify(final InventorySlot slot, SlotEvent event);
}
