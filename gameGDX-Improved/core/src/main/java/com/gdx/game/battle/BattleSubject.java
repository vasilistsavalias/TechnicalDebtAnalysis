package com.gdx.game.battle;

import com.badlogic.gdx.utils.Array;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEvents.BattleEvent;
import com.gdx.game.entities.Entity;

public class BattleSubject {
    private Array<BattleEntity> observers;

    public BattleSubject() {
        observers = new Array<>();
    }

    public void addObserver(BattleEntity battleEntity) {
        observers.add(battleEntity);
    }

    public void removeObserver(BattleEntity battleEntity) {
        observers.removeValue(battleEntity, true);
    }

    protected void notify( Entity entity, BattleEvent event) {
        for(BattleEntity observer: observers) {
            observer.onNotify(entity, event);
        }
    }
}
