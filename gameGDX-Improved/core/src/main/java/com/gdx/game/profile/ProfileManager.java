package com.gdx.game.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.inventory.InventoryItem;
import com.gdx.game.inventory.InventoryItemLocation;
import com.gdx.game.inventory.InventoryUI;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;
import com.gdx.game.quest.QuestGraph;
import com.gdx.game.quest.QuestUI;
import com.gdx.game.status.StatusUI;

import java.util.Enumeration;
import java.util.Hashtable;

public class ProfileManager extends ProfileSubject {

    private Json json;
    private static ProfileManager profileManager;
    private Hashtable<String,FileHandle> profiles;
    private ObjectMap<String, Object> profileProperties = new ObjectMap<>();
    private String profileName;
    private boolean isNewProfile = false;

    private static final String SAVEGAME_SUFFIX = ".sav";
    public static final String DEFAULT_PROFILE = "default";

    public ProfileManager() {
        json = new Json();
        profiles = new Hashtable<>();
        profiles.clear();
        profileName = DEFAULT_PROFILE;
        storeAllProfiles();
    }

    public static ProfileManager getInstance() {
        if(profileManager == null) {
            profileManager = new ProfileManager();
        }
        return profileManager;
    }

    public void setIsNewProfile(boolean isNewProfile) {
        this.isNewProfile = isNewProfile;
    }

    public boolean getIsNewProfile() {
        return this.isNewProfile;
    }

    public Array<String> getProfileList() {
        Array<String> profiles = new Array<>();
        for(Enumeration<String> e = this.profiles.keys(); e.hasMoreElements();) {
            profiles.add(e.nextElement());
        }
        return profiles;
    }

    public FileHandle getProfileFile(String profile) {
        if(!doesProfileExist(profile)) {
            return null;
        }
        return profiles.get(profile);
    }

    public void storeAllProfiles() {
        if(Gdx.files.isLocalStorageAvailable()) {
            FileHandle[] files = Gdx.files.local(".").list(SAVEGAME_SUFFIX);

            for(FileHandle file: files) {
                profiles.put(file.nameWithoutExtension(), file);
            }
        } else {
            //TODO: try external directory here
            return;
        }
    }

    public boolean doesProfileExist(String profileName) {
        return profiles.containsKey(profileName);
    }

    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite) {
        String fullFilename = profileName + SAVEGAME_SUFFIX;

        boolean localFileExists = Gdx.files.local(fullFilename).exists();

        //If we cannot overwrite and the file exists, exit
        if(localFileExists && !overwrite) {
            return;
        }

        FileHandle file =  null;

        if(Gdx.files.isLocalStorageAvailable()) {
            file = Gdx.files.local(fullFilename);
            String encodedString = Base64Coder.encodeString(fileData);
            file.writeString(encodedString, !overwrite);
        }

        profiles.put(profileName, file);
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public <T extends Object> T getProperty(String key, Class<T> type){
        T property = null;
        if(!profileProperties.containsKey(key)) {
            return property;
        }
        property = (T) profileProperties.get(key);
        return property;
    }

    public void saveProfile() {
        notify(this, ProfileObserver.SAVING_PROFILE);
        String text = json.prettyPrint(json.toJson(profileProperties));
        writeProfileToStorage(profileName, text, true);
    }

    public void loadProfile() {
        if(isNewProfile) {
            notify(this, ProfileObserver.CLEAR_CURRENT_PROFILE);
            saveProfile();
        }

        String fullProfileFileName = profileName + SAVEGAME_SUFFIX;
        boolean doesProfileFileExist = Gdx.files.local(fullProfileFileName).exists();

        if(!doesProfileFileExist) {
            return;
        }

        FileHandle encodedFile = profiles.get(profileName);
        String s = encodedFile.readString();

        String decodedFile = Base64Coder.decodeString(s);

        profileProperties = json.fromJson(ObjectMap.class, decodedFile);
        notify(this, ProfileObserver.PROFILE_LOADED);
        isNewProfile = false;
    }

    public void setCurrentProfile(String profileName) {
        if(doesProfileExist(profileName)) {
            this.profileName = profileName;
        } else {
            this.profileName = DEFAULT_PROFILE;
        }
    }

    public void setLoadedProfile(PlayerHUD playerHUD) {
        if (isNewProfile) {
            playerHUD.setNewProfile(this);
        } else {
            setOldProfile(playerHUD);
        }
    }

    public void setOldProfile(PlayerHUD playerHUD) {
        Array<InventoryItemLocation> inventory = getProperty("playerInventory", Array.class);
        Array<InventoryItemLocation> equipInventory = getProperty("playerEquipInventory", Array.class);
        Array<QuestGraph> quests = getProperty("playerQuests", Array.class);
        int goldVal = getProperty("currentPlayerGP", Integer.class);
        int xpMaxVal = getProperty("currentPlayerXPMax", Integer.class);
        int xpVal = getProperty("currentPlayerXP", Integer.class);
        int hpMaxVal = getProperty("currentPlayerHPMax", Integer.class);
        int hpVal = getProperty("currentPlayerHP", Integer.class);
        int mpMaxVal = getProperty("currentPlayerMPMax", Integer.class);
        int mpVal = getProperty("currentPlayerMP", Integer.class);
        int levelVal = getProperty("currentPlayerLevel", Integer.class);
        playerHUD.setOldProfile(inventory, equipInventory, quests, goldVal, xpMaxVal, xpVal, hpMaxVal, hpVal, mpMaxVal, mpVal, levelVal);
    }
    public void setJsonPropertiesToSaveProfile(PlayerHUD playerHUD) {
        QuestUI questUI = playerHUD.getQuestUI();
        InventoryUI inventoryUI = playerHUD.getInventoryUI();
        MapManager mapManager = playerHUD.getMapManager();
        StatusUI statusUI = playerHUD.getStatusUI();

        setProperty("playerQuests", questUI.getQuests());
        setProperty("playerInventory", InventoryUI.getInventory(inventoryUI.getInventorySlotTable()));
        setProperty("playerEquipInventory", InventoryUI.getInventory(inventoryUI.getEquipSlotTable()));
        if(mapManager.getPlayer() != null) {
            setProperty("playerCharacter", EntityFactory.EntityType.valueOf(mapManager.getPlayer().getEntityConfig().getEntityID()));
        }
        setProperty("currentPlayerGP", statusUI.getGoldValue());
        setProperty("currentPlayerLevel", statusUI.getLevelValue());
        setProperty("currentPlayerXP", statusUI.getXPValue());
        setProperty("currentPlayerXPMax", statusUI.getXPValueMax());
        if(statusUI.getHPValue() != 0) {
            setProperty("currentPlayerHP", statusUI.getHPValue());
        }
        setProperty("currentPlayerHPMax", statusUI.getHPValueMax());
        setProperty("currentPlayerMP", statusUI.getMPValue());
        setProperty("currentPlayerMPMax", statusUI.getMPValueMax());
        setProperty("currentPlayerAP", inventoryUI.getAPVal());
        setProperty("currentPlayerDP", inventoryUI.getDPVal());
    }
    public void setJsonPropertiesToSaveMap(MapManager mapManager) {
        if(mapManager.getCurrentMap() != null) {
            setProperty("currentMapType", mapManager.getCurrentMapType().toString());
        }

        setProperty("currentPlayerPosition", mapManager.getPlayer().getCurrentPosition());
        setProperty("toppleMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOPPLE).getPlayerStart());
        setProperty("toppleRoad1MapStartPosition", MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).getPlayerStart());

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            setProperty("currentPlayerPosition", null);
        }
    }

}
