package com.gdx.game.events.ProfileEvents;

import com.gdx.game.events.Event;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;

public abstract class ProfileEvent implements Event {
    public abstract void ProfileManagerActivation(ProfileManager profileManager, PlayerHUD playerHUD);
    public abstract void ProfileManagerActivation(ProfileManager profileManager, MapManager mapManager);
}
