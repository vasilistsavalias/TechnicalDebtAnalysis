package com.gdx.game.events.SlotEvents;

import com.gdx.game.inventory.InventoryItem;
import com.gdx.game.inventory.InventoryObserver;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.inventory.slot.InventorySlot;
import com.gdx.game.inventory.store.StoreInventoryUI;
import com.gdx.game.profile.ProfileManager;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AddedItemEvent extends SlotEvent{
    @Override
    public void PerformInventoryUIAction(InventorySlot slot, InventoryUI inventoryUI) {
        int APVal = inventoryUI.getAPVal();
        Label APValLabel = inventoryUI.getAPValLabel();
        int DPVal = inventoryUI.getDPVal();
        Label DPValLabel = inventoryUI.getDPValLabel();
        InventoryItem addItem = slot.getTopInventoryItem();
        if(addItem != null) {
            return;
        }
        if(addItem.isInventoryItemOffensive()) {
            APVal += addItem.getItemUseTypeValue();
            APValLabel.setText(String.valueOf(APVal));
            inventoryUI.notify(String.valueOf(APVal), InventoryObserver.InventoryEvent.UPDATED_AP);
            ProfileManager.getInstance().setProperty("currentPlayerAP", APVal);
            if(addItem.isInventoryItemOffensiveWand()) {
                inventoryUI.notify(String.valueOf(addItem.getItemUseTypeValue()), InventoryObserver.InventoryEvent.ADD_WAND_AP);
            }
        } else if(addItem.isInventoryItemDefensive()) {
            DPVal += addItem.getItemUseTypeValue();
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

        //moving from player inventory to store inventory to sell
        if(slot.getTopInventoryItem().getName().equalsIgnoreCase(InventoryUI.PLAYER_INVENTORY) &&
                slot.getName().equalsIgnoreCase(InventoryUI.STORE_INVENTORY)) {
            tradeInVal += slot.getTopInventoryItem().getTradeValue();
            sellTotalLabel.setText("SELL : " + tradeInVal + "GP");
        }
        //moving from store inventory to player inventory to buy
        if(slot.getTopInventoryItem().getName().equalsIgnoreCase(InventoryUI.STORE_INVENTORY) &&
                slot.getName().equalsIgnoreCase(InventoryUI.PLAYER_INVENTORY)) {
            fullValue += slot.getTopInventoryItem().getItemValue();
            buyTotalLabel.setText("BUY : " + fullValue + "GP");
        }
    }
}
