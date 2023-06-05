package com.gdx.game.entities.player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdx.game.component.Component;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.entities.player.Layers.LayerActivation;
import com.gdx.game.map.Map;
import com.gdx.game.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPhysicsComponent extends PhysicsComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerPhysicsComponent.class);

    private Entity.State state;
    private Vector3 mouseSelectCoordinates;
    private boolean isMouseSelectEnabled = false;
    private String previousDiscovery;
    private String previousEnemySpawn;

    public PlayerPhysicsComponent() {
        super.velocity = new Vector2(5f, 5f);
        boundingBoxLocation = PhysicsComponent.BoundingBoxLocation.BOTTOM_CENTER;
        initBoundingBox(0.3f, 0.5f);
        previousDiscovery = "";
        previousEnemySpawn = "0";

        mouseSelectCoordinates = new Vector3(0,0,0);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);
        if(string.length == 2) {
            //Specifically for messages with 1 object payload
            if(string[0].equalsIgnoreCase(Component.MESSAGE.INIT_START_POSITION.toString())) {
                currentEntityPosition = json.fromJson(Vector2.class, string[1]);
                nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
                previousDiscovery = "";
                previousEnemySpawn = "0";
                notify(previousEnemySpawn, ComponentObserver.ComponentEvent.ENEMY_SPAWN_LOCATION_CHANGED);
            } else if(string[0].equalsIgnoreCase(Component.MESSAGE.CURRENT_STATE.toString())) {
                state = json.fromJson(Entity.State.class, string[1]);
            } else if(string[0].equalsIgnoreCase(Component.MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            } else if(string[0].equalsIgnoreCase(Component.MESSAGE.INIT_SELECT_ENTITY.toString())) {
                mouseSelectCoordinates = json.fromJson(Vector3.class, string[1]);
                isMouseSelectEnabled = true;
            } else if(string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_ENTITY.toString())) {
                entityEncounteredType = json.fromJson(EntityFactory.EntityName.class, string[1]);
            } else if(string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_FOE.toString())) {
                entityEncounteredType = json.fromJson(EntityFactory.EntityName.class, string[1]);
                notify(entityEncounteredType.name(), ComponentObserver.ComponentEvent.START_BATTLE);
            }
        }
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta) {
        //We want the hit-box to be at the feet for a better feel
        updateBoundingBoxPosition(nextEntityPosition);
        LayerActivation.updateLayerActivation(this, mapMgr);
        if(isMouseSelectEnabled) {
            selectMapEntityCandidate(mapMgr);
            isMouseSelectEnabled = false;
        }

        if(!isCollisionWithMapLayer(entity, mapMgr) && !isCollisionWithMapEntities(entity, mapMgr) && state == Entity.State.WALKING) {
            setNextPositionToCurrent(entity);

            Camera camera = mapMgr.getCamera();
            camera.position.set(currentEntityPosition.x, currentEntityPosition.y, 0f);
            camera.update();
        } else {
            updateBoundingBoxPosition(currentEntityPosition);
        }

        calculateNextPosition(delta);
    }

    private void selectMapEntityCandidate(MapManager mapMgr) {
        tempEntities.clear();
        tempEntities.addAll(mapMgr.getCurrentMapEntities());
        tempEntities.addAll(mapMgr.getCurrentMapQuestEntities());

        //Convert screen coordinates to world coordinates, then to unit scale coordinates
        mapMgr.getCamera().unproject(mouseSelectCoordinates);
        mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        for(Entity mapEntity : tempEntities) {
            //Don't break, reset all entities
            mapEntity.sendMessage(Component.MESSAGE.ENTITY_DESELECTED);
            Rectangle mapEntityBoundingBox = mapEntity.getCurrentBoundingBox();
            if(mapEntity.getCurrentBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y)) {
                //Check distance
                Vector3 vec3Player = new Vector3(boundingBox.x, boundingBox.y, 0);
                Vector3 vec3Npc = new Vector3(mapEntityBoundingBox.x, mapEntityBoundingBox.y, 0);
                float distance = vec3Player.dst(vec3Npc);

                if(distance <= SELECT_RAY_MAXIMUM_DISTANCE) {
                    //We have a valid entity selection
                    //Picked/Selected
                    LOGGER.debug("Selected Entity! " + mapEntity.getEntityConfig().getEntityID());
                    mapEntity.sendMessage(Component.MESSAGE.ENTITY_SELECTED);
                    notify(json.toJson(mapEntity.getEntityConfig()), ComponentObserver.ComponentEvent.LOAD_CONVERSATION);
                }
            }
        }
        tempEntities.clear();
    }

    public boolean isPreviousEnemySpawnEquals(String value) {
        return previousEnemySpawn.equalsIgnoreCase(value);
    }

    public boolean isBoundingBoxOverlaps(Rectangle rectangle) {
        return boundingBox.overlaps(rectangle);
    }

    public void printLOGGER(String message) {
        LOGGER.debug(message);
    }

    public void callLOGGER(String enemySpawnID) {
        printLOGGER("Enemy Spawn Area " + enemySpawnID + " Activated with previous Spawn value: " + previousEnemySpawn);
        previousEnemySpawn = enemySpawnID;
    }

    public void resetLOGGER(String value) {
        printLOGGER("Enemy Spawn Area RESET with previous value " + previousEnemySpawn);
        previousEnemySpawn = value;
    }

    public boolean isPreviousDiscoveryEquals(String val) {
        return previousDiscovery.equalsIgnoreCase(val);
    }

    public void setPreviousDiscovery(String val) {
        previousDiscovery = (!isPreviousDiscoveryEquals(val)) ? val : previousDiscovery;
    }

    public String toJson(String val) {
        return json.toJson(val);
    }

    public String getMESSAGE_TOKEN() {
        return MESSAGE_TOKEN;
    }

    public Vector2 getCurrentEntityPosition() {
        return currentEntityPosition;
    }

    public void setCurrentEntityPosition(Vector2 position) {
        currentEntityPosition = position;
    }

    public void setNextEntityPosition(Vector2 position) {
        nextEntityPosition = position;
    }

}
