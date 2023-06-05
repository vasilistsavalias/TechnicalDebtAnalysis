package com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.entities.EntityConfig;
import static com.badlogic.gdx.net.HttpRequestBuilder.json;
import static com.gdx.game.battle.BattleHUD.notificationUI;

public class LoadResumeEvent extends ComponentEvent {

    @Override
    public void activateEvent(String value, BattleHUD componentEvent) {
        EntityConfig config = json.fromJson(EntityConfig.class, value);
        notificationUI.loadResume(config);

    }


    @Override
    public void onNotify(String value, ComponentEvent event) {

    }
}
