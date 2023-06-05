package com.gdx.game.events.ProfileEvents;

import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.inventory.InventoryItemLocation;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;

public class ProfileClearCurrentEvent extends ProfileEvent {
    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, PlayerHUD playerHUD) {
        profileManager.setProperty("playerInventory", new Array<InventoryItemLocation>());
        profileManager.setProperty("playerEquipInventory", new Array<InventoryItemLocation>());
        profileManager.setProperty("playerCharacter", null);
        profileManager.setProperty("currentPlayerGP", 0);
        profileManager.setProperty("currentPlayerLevel",0);
        profileManager.setProperty("currentPlayerXP", 0);
        profileManager.setProperty("currentPlayerXPMax", 0);
        profileManager.setProperty("currentPlayerHP", 0);
        profileManager.setProperty("currentPlayerHPMax", 0);
        profileManager.setProperty("currentPlayerMP", 0);
        profileManager.setProperty("currentPlayerMPMax", 0);
        profileManager.setProperty("currentPlayerAP", 0);
        profileManager.setProperty("currentPlayerDP", 0);
        profileManager.setProperty("currentTime", 0);
    }

    @Override
    public void ProfileManagerActivation(ProfileManager profileManager, MapManager mapManager) {
        mapManager.setCurrentMap(null);
        profileManager.setProperty("currentPlayerPosition", null);
        profileManager.setProperty("currentMapType", MapFactory.MapType.TOPPLE.toString());
        MapFactory.clearCache();
        profileManager.setProperty("toppleMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOPPLE).getPlayerStart());
        profileManager.setProperty("toppleRoad1MapStartPosition", MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).getPlayerStart());
    }
}
