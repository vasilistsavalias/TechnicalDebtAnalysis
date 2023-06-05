package com.gdx.game.entities.player.Layers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.entities.player.PlayerPhysicsComponent;
import com.gdx.game.map.MapFactory;
import com.gdx.game.map.MapManager;

public interface LayerActivation {
    static boolean[] updateLayerActivation(PlayerPhysicsComponent ppComponent, MapManager mapMgr) {
        return new boolean[]
                {
                        PortalLayer.Activate(ppComponent, mapMgr),
                        DiscoverLayer.Activate(ppComponent, mapMgr),
                        EnemySpawnLayer.Activate(ppComponent, mapMgr)
                };
    }
}
