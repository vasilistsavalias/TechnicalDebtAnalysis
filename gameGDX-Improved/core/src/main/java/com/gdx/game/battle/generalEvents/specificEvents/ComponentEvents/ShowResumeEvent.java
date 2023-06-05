package com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.entities.EntityConfig;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;
import static com.gdx.game.battle.BattleHUD.notificationUI;

public class ShowResumeEvent extends ComponentEvent {

    @Override
    public void activateEvent(String value, BattleHUD componentEvent) {
        EntityConfig configShow = json.fromJson(EntityConfig.class, value);

        if (configShow.getEntityID().equalsIgnoreCase(notificationUI.getCurrentEntityID())) {
            notificationUI.setVisible(true);
        }
    }

    @Override
    public void onNotify(String value, ComponentEvent event) {

    }
}
