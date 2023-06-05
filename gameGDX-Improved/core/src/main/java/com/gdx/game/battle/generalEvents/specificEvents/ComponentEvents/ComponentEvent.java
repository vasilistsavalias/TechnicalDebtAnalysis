package com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents;

import com.gdx.game.battle.generalEvents.specificEvents.ComponentEntity;
import com.gdx.game.battle.BattleHUD;

public abstract class ComponentEvent implements ComponentEntity {

    public abstract void activateEvent(String value, BattleHUD componentEvent);


}
