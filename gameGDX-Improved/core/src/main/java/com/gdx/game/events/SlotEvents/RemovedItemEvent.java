package com.gdx.game.events.SlotEvents;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gdx.game.inventory.InventoryItem;
import com.gdx.game.inventory.InventoryObserver;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.inventory.slot.InventorySlot;
import com.gdx.game.inventory.store.StoreInventoryUI;
import com.gdx.game.profile.ProfileManager;

public class RemovedItemEvent extends SlotEvent{
    @Override
    public void PerformInventoryUIAction(InventorySlot slot, InventoryUI inventoryUI) {
        int APVal = inventoryUI.getAPVal();
        Label APValLabel = inventoryUI.getAPValLabel();
        int DPVal = inventoryUI.getDPVal();
        Label DPValLabel = inventoryUI.getDPValLabel();
        InventoryItem removeItem = slot.getTopInventoryItem();
        if(removeItem == null) {
            return;
        }
        if(removeItem.isInventoryItemOffensive()) {
            APVal -= removeItem.getItemUseTypeValue();
            APValLabel.setText(String.valueOf(APVal));
            inventoryUI.notify(String.valueOf(APVal), InventoryObserver.InventoryEvent.UPDATED_AP);
            ProfileManager.getInstance().setProperty("currentPlayerAP", APVal);
            if(removeItem.isInventoryItemOffensiveWand()) {
                inventoryUI.notify(String.valueOf(removeItem.getItemUseTypeValue()), InventoryObserver.InventoryEvent.REMOVE_WAND_AP);
            }
        } else if(removeItem.isInventoryItemDefensive()) {
            DPVal -= removeItem.getItemUseTypeValue();
            DPValLabel.setText(String.valueOf(DPVal));
            inventoryUI.notify(String.valueOf(DPVal), InventoryObserver.InventoryEvent.UPDATED_DP);
            ProfileManager.getInstance().setProperty("currentPlayerDP", DPVal);
        }
    }

    @Override
    public void PerformStoreInventoryUIAction(InventorySlot slot, StoreInventoryUI storeInventoryUI) {
        int tradeInVal = storeInventoryUI.getTradeInVal();
        int fullValue = storeInventoryUI.getFullValue();
        Label sellTotalLabel = storeInventoryUI.getSellTotalLabel();
        Label buyTotalLabel = storeInventoryUI.getBuyTotalLabel();
        if(slot.getTopInventoryItem().getName().equalsIgnoreCase(InventoryUI.PLAYER_INVENTORY) && slot.getName().equalsIgnoreCase(InventoryUI.STORE_INVENTORY)) {
            tradeInVal -= slot.getTopInventoryItem().getTradeValue();
            sellTotalLabel.setText("SELL : " + tradeInVal + "GP");
        }
        if(slot.getTopInventoryItem().getName().equalsIgnoreCase(InventoryUI.STORE_INVENTORY) && slot.getName().equalsIgnoreCase(InventoryUI.PLAYER_INVENTORY)) {
            fullValue -= slot.getTopInventoryItem().getItemValue();
            buyTotalLabel.setText("BUY : " + fullValue + "GP");
        }
    }
}
