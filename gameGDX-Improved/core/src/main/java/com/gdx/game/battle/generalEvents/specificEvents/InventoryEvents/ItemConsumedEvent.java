package com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.component.Component;
import com.gdx.game.inventory.InventoryItem;

public class ItemConsumedEvent extends InventoryEvent {
    @Override
    public void onNotify(String value, InventoryEvent event) {

    }

    @Override
    public void activateEvent(String value, BattleHUD inventoryEvent) {
        String[] strings = value.split(Component.MESSAGE_TOKEN);
        if(strings.length != 2) {
            return;
        }

        int type = Integer.parseInt(strings[0]);
        int typeValue = Integer.parseInt(strings[1]);

        if(InventoryItem.doesRestoreHP(type)) {
            //notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_EATING);
            inventoryEvent.battleStatusUI.addHPValue(typeValue);
        } else if(InventoryItem.doesRestoreMP(type)) {
            //notify(AudioObserver.AudioCommand.SOUND_PLAY_ONCE, AudioObserver.AudioTypeEvent.SOUND_DRINKING);
            inventoryEvent.battleStatusUI.addMPValue(typeValue);
        }
    }
}
