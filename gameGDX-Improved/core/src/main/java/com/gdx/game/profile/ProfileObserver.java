package com.gdx.game.profile;

import com.gdx.game.events.ProfileEvents.ProfileClearCurrentEvent;
import com.gdx.game.events.ProfileEvents.ProfileEvent;
import com.gdx.game.events.ProfileEvents.ProfileLoadedEvent;
import com.gdx.game.events.ProfileEvents.ProfileSavingEvent;

public interface ProfileObserver {
    ProfileEvent PROFILE_LOADED = new ProfileLoadedEvent();
    ProfileEvent SAVING_PROFILE = new ProfileSavingEvent();
    ProfileEvent CLEAR_CURRENT_PROFILE = new ProfileClearCurrentEvent();

    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}
