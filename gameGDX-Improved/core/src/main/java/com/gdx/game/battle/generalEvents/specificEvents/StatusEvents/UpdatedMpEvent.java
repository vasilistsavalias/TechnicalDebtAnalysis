package com.gdx.game.battle.generalEvents.specificEvents.StatusEvents;

import com.gdx.game.battle.BattleHUD;
import com.gdx.game.profile.ProfileManager;

public class UpdatedMpEvent extends StatusEvent {


    @Override
    public void activateEvent(int value, BattleHUD StatusEvent) {
        ProfileManager.getInstance().setProperty("currentPlayerMP",StatusEvent.battleStatusUI.getMPValue());
    }

    @Override
    public void onNotify(int value, StatusEvent event) {

    }
}
