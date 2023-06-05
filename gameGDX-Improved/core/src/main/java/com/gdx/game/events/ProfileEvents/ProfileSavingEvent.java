package com.gdx.game.events.ProfileEvents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.quest.QuestUI;
import com.gdx.game.status.StatusUI;

public class ProfileSavingEvent extends ProfileEvent {
    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, PlayerHUD playerHUD) {
        profileManager.setJsonPropertiesToSaveProfile(playerHUD);
    }

    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, MapManager mapManager) {
        profileManager.setJsonPropertiesToSaveMap(mapManager);
    }
}
