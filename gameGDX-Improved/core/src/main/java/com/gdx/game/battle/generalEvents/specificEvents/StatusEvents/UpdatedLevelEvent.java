package com.gdx.game.battle.generalEvents.specificEvents.StatusEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.profile.ProfileManager;

public class UpdatedLevelEvent extends StatusEvent {


    @Override
    public void activateEvent(int value, BattleHUD StatusEvent) {
        ProfileManager.getInstance().setProperty("currentPlayerLevel", StatusEvent.battleStatusUI.getLevelValue());
        StatusEvent.createStatsUpUI(StatusEvent.battleStatusUI.getNbrLevelUp());
    }

    @Override
    public void onNotify(int value, StatusEvent event) {

    }
}
