package com.gdx.game.entities.player.Layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.entities.player.PlayerPhysicsComponent;
import com.gdx.game.map.MapManager;

public class EnemySpawnLayer {
    public static boolean Activate(PlayerPhysicsComponent ppComponent, MapManager mapMgr) {
        MapLayer mapEnemySpawnLayer =  mapMgr.getEnemySpawnLayer();
        if (mapEnemySpawnLayer != null) {
            Rectangle rectangle;
            for(MapObject object: mapEnemySpawnLayer.getObjects()) {
                if(object instanceof RectangleMapObject) {
                    rectangle = ((RectangleMapObject)object).getRectangle();
                    if(ppComponent.isBoundingBoxOverlaps(rectangle)) {
                        String enemySpawnID = object.getName();
                        if (enemySpawnID != null && ppComponent.isPreviousEnemySpawnEquals(enemySpawnID)) {
                            ppComponent.notify(enemySpawnID, ComponentObserver.ComponentEvent.ENEMY_SPAWN_LOCATION_CHANGED);
                            return true;
                        }
                        ppComponent.callLOGGER(enemySpawnID);
                        return false;
                    }
                }
            }
            //If no collision, reset the value
            if(!ppComponent.isPreviousEnemySpawnEquals(String.valueOf(0))) {
                ppComponent.resetLOGGER(String.valueOf(0));
                ppComponent.notify(String.valueOf(0), ComponentObserver.ComponentEvent.ENEMY_SPAWN_LOCATION_CHANGED);
            }
        }
        return false;
    }
}
