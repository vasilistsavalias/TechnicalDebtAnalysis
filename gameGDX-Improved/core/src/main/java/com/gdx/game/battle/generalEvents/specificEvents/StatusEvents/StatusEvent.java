package com.gdx.game.battle.generalEvents.specificEvents.StatusEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEntity;

public abstract class StatusEvent implements StatusEntity {
    public abstract void activateEvent(int value, BattleHUD StatusEvent);
}
