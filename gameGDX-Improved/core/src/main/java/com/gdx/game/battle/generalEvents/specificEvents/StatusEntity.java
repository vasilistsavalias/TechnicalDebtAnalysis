package com.gdx.game.battle.generalEvents.specificEvents;

import com.gdx.game.battle.generalEvents.GeneralEntity;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.*;
//import com.gdx.game.status.StatusObserver;
import jdk.jshell.Snippet;

public interface StatusEntity extends GeneralEntity {

    StatusEvent updatedLevel = new UpdatedLevelEvent();
    StatusEvent updatedLevelFromQuest = new UpdatedLevelFromQuestEvent();
    StatusEvent updatedHP = new UpdatedHpEvent();
    StatusEvent updatedMP = new UpdatedMpEvent();
    StatusEvent updatedXP = new UpdatedXPEvent();
    StatusEvent leveledUp = new leveledUpEvent();


    public void onNotify(int value, StatusEvent event);
}

