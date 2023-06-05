package com.gdx.game.entities.player.Layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.entities.player.PlayerPhysicsComponent;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;

public class PortalLayer {
    public static boolean Activate(PlayerPhysicsComponent ppComponent, MapManager mapMgr) {
        MapLayer mapPortalLayer =  mapMgr.getPortalLayer();

        if (mapPortalLayer != null) {
            Rectangle rectangle;
            for(MapObject object: mapPortalLayer.getObjects()) {
                if(object instanceof RectangleMapObject) {
                    rectangle = ((RectangleMapObject)object).getRectangle();
                    if(ppComponent.isBoundingBoxOverlaps(rectangle)) {
                        String mapName = object.getName();
                        if(mapName == null) {
                            return false;
                        }
                        mapMgr.setClosestStartPositionFromScaledUnits(ppComponent.getCurrentEntityPosition());
                        mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));
                        ppComponent.setCurrentEntityPosition(new Vector2(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y));
                        ppComponent.setNextEntityPosition(new Vector2(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y));
                        ppComponent.printLOGGER("Portal Activated");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
