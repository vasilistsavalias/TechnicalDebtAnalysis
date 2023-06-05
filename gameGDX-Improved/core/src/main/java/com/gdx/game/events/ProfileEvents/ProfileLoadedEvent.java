package com.gdx.game.events.ProfileEvents;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.inventory.InventoryItem;
import com.gdx.game.inventory.InventoryItemLocation;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.quest.QuestGraph;
import com.gdx.game.quest.QuestUI;
import com.gdx.game.status.StatusUI;

public class ProfileLoadedEvent extends ProfileEvent{
    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, PlayerHUD playerHUD) {
        profileManager.setLoadedProfile(playerHUD);
    }

    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, MapManager mapManager) {
        mapManager.loadMapFromManager(profileManager);
        mapManager.setPlayerStartPosition(profileManager);
    }
}
