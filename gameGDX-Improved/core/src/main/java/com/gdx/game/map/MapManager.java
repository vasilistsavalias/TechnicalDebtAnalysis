package com.gdx.game.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.component.Component;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.entities.Entity;
import com.gdx.game.events.ProfileEvents.ProfileEvent;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.profile.ProfileObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapManager implements ProfileObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    private Camera camera;
    private boolean mapChanged = false;
    private Map currentMap;
    private Entity player;
    private Entity currentSelectedEntity = null;

    public MapManager() {
        // Nothing
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map newMap) {
        currentMap = newMap;
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        event.ProfileManagerActivation(profileManager, this);
    }

    public void loadMap(MapFactory.MapType mapType) {
        Map map = MapFactory.getMap(mapType);

        if(map == null) {
            LOGGER.debug("Map does not exist!");
            return;
        }

        if(currentMap != null && currentMap.getMusicTheme() != map.getMusicTheme()) {
            currentMap.unloadMusic();
        }

        map.loadMusic();

        currentMap = map;
        mapChanged = true;
        clearCurrentSelectedMapEntity();
        LOGGER.debug("Player Start: (" + currentMap.getPlayerStart().x + "," + currentMap.getPlayerStart().y + ")");
    }

    public void unregisterCurrentMapEntityObservers() {
        if(currentMap != null) {
            Array<Entity> entities = currentMap.getMapEntities();
            for(Entity entity: entities) {
                entity.unregisterObservers();
            }

            Array<Entity> questEntities = currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities) {
                questEntity.unregisterObservers();
            }
        }
    }

    public void registerCurrentMapEntityObservers(ComponentObserver observer) {
        if(currentMap != null) {
            Array<Entity> entities = currentMap.getMapEntities();
            for(Entity entity: entities) {
                entity.registerObserver(observer);
            }

            Array<Entity> questEntities = currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities) {
                questEntity.registerObserver(observer);
            }
        }
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer() {
        return currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer() {
        return currentMap.getPortalLayer();
    }

    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID) {
        return currentMap.getQuestItemSpawnPositions(objectName, objectTaskID);
    }

    public MapLayer getQuestDiscoverLayer() {
        return currentMap.getQuestDiscoverLayer();
    }

    public MapLayer getEnemySpawnLayer() {
        return currentMap.getEnemySpawnLayer();
    }

    public MapFactory.MapType getCurrentMapType() {
        return currentMap.getCurrentMapType();
    }

    public Vector2 getPlayerStartUnitScaled() {
        return currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap() {
        if(currentMap == null) {
            loadMap(MapFactory.MapType.TOPPLE);
        }
        return currentMap.getCurrentTiledMap();
    }

    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta) {
        currentMap.updateMapEntities(mapMgr, batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities() {
        return currentMap.getMapEntities();
    }

    public final Array<Entity> getCurrentMapQuestEntities() {
        return currentMap.getMapQuestEntities();
    }

    public void addMapQuestEntities(Array<Entity> entities) {
        currentMap.getMapQuestEntities().addAll(entities);
    }

    public void removeMapQuestEntity(Entity entity) {
        entity.unregisterObservers();

        Array<Vector2> positions = ProfileManager.getInstance().getProperty(entity.getEntityConfig().getEntityID(), Array.class);
        if(positions == null) {
            return;
        }

        for(Vector2 position : positions) {
            if(position.x == entity.getCurrentPosition().x && position.y == entity.getCurrentPosition().y) {
                positions.removeValue(position, true);
                break;
            }
        }
        currentMap.getMapQuestEntities().removeValue(entity, true);
        ProfileManager.getInstance().setProperty(entity.getEntityConfig().getEntityID(), positions);
    }

    public void addMapEntity(Entity entity) {
        currentMap.getMapEntities().add(entity);
    }

    public void removeMapEntity(Entity entity) {
        entity.unregisterObservers();

        Vector2 position = entity.getCurrentPosition();
        if(position == null) {
            return;
        }

        currentMap.getMapEntities().removeValue(entity, true);
    }

    public void clearAllMapQuestEntities() {
        currentMap.getMapQuestEntities().clear();
    }

    public Entity getCurrentSelectedMapEntity() {
        return currentSelectedEntity;
    }

    public void setCurrentSelectedMapEntity(Entity currentSelectedEntity) {
        this.currentSelectedEntity = currentSelectedEntity;
    }

    public void clearCurrentSelectedMapEntity() {
        if(currentSelectedEntity == null) {
            return;
        }
        currentSelectedEntity.sendMessage(Component.MESSAGE.ENTITY_DESELECTED);
        currentSelectedEntity = null;
    }

    public void setPlayer(Entity entity) {
        this.player = entity;
    }

    public Entity getPlayer() {
        return this.player;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean hasMapChanged() {
        return mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged) {
        this.mapChanged = hasMapChanged;
    }
    public void loadMapFromManager(ProfileManager profileManager) {
        String currentMap = profileManager.getProperty("currentMapType", String.class);
        MapFactory.MapType mapType;
        if(currentMap == null || currentMap.isEmpty()) {
            mapType = MapFactory.MapType.TOPPLE;
        } else {
            mapType = MapFactory.MapType.valueOf(currentMap);
        }
        loadMap(mapType);
    }
    public void setPlayerStartPosition(ProfileManager profileManager) {
        Vector2 toppleRoad1MapStartPosition = profileManager.getProperty("toppleRoad1MapStartPosition", Vector2.class);
        if(toppleRoad1MapStartPosition != null) {
            MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).setPlayerStart(toppleRoad1MapStartPosition);
        }

        Vector2 toppleMapStartPosition = profileManager.getProperty("toppleMapStartPosition", Vector2.class);
        if(toppleMapStartPosition != null) {
            MapFactory.getMap(MapFactory.MapType.TOPPLE).setPlayerStart(toppleMapStartPosition);
        }

        Vector2 currentPlayerPosition = profileManager.getProperty("currentPlayerPosition", Vector2.class);
        if(currentPlayerPosition != null && !currentPlayerPosition.equals(new Vector2(0, 0))) {
            Vector2 currentPositionOnMap = new Vector2(currentPlayerPosition.x*16, currentPlayerPosition.y*16);
            MapFactory.getMap(getCurrentMapType()).setPlayerStart(currentPositionOnMap);
        }
    }
}
