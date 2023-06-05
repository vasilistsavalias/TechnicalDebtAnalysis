package com.gdx.game.battle.generalEvents.specificEvents.StatusEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.profile.ProfileManager;

public class UpdatedXPEvent extends StatusEvent {

    @Override
    public void activateEvent(int value, BattleHUD StatusEvent) {
        ProfileManager.getInstance().setProperty("currentPlayerXP", StatusEvent.battleStatusUI.getXPValue());
    }

    @Override
    public void onNotify(int value, StatusEvent event) {

    }
}
