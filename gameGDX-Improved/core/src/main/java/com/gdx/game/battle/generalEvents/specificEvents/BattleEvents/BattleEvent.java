package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.entities.Entity;

public abstract class BattleEvent  {

    public abstract void activateEvent(Entity entity, BattleHUD battleHUD);
}
