package com.gdx.game.entities.player.Layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.entities.player.PlayerPhysicsComponent;
import com.gdx.game.map.MapManager;

public class DiscoverLayer {
    public static boolean Activate(PlayerPhysicsComponent ppComponent, MapManager mapMgr) {
        MapLayer mapDiscoverLayer =  mapMgr.getQuestDiscoverLayer();
        if (mapDiscoverLayer != null) {
            Rectangle rectangle;
            for(MapObject object: mapDiscoverLayer.getObjects()) {
                if(object instanceof RectangleMapObject) {
                    rectangle = ((RectangleMapObject)object).getRectangle();

                    if(ppComponent.isBoundingBoxOverlaps(rectangle)) {
                        String questID = object.getName();
                        String questTaskID = (String)object.getProperties().get("taskID");
                        String val = questID + ppComponent.getMESSAGE_TOKEN() + questTaskID;
                        if(questID == null) {
                            break;
                        }
                        ppComponent.setPreviousDiscovery(val);
                        ppComponent.notify(ppComponent.toJson(val), ComponentObserver.ComponentEvent.QUEST_LOCATION_DISCOVERED);
                        ppComponent.printLOGGER("Discover Area Activated");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
