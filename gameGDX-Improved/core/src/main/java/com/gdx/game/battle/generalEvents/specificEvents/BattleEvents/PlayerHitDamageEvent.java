package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.screen.GameScreen;

public class PlayerHitDamageEvent extends BattleEvent {


    @Override
    public void onNotify(Entity enemyEntity, BattleEvent event) {

    }


    public void onNotify(String value, ComponentEvent event) {

    }


    public void onNotify(String value, InventoryEvent event) {

    }


    public void onNotify(int value, StatusEvent event) {

    }

    @Override
    public void activateEvent(Entity entity, BattleHUD battleHUD) {
        int damagePlayer = Integer.parseInt(battleHUD.player.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
        battleHUD.getDmgPlayerValLabel().setText(String.valueOf(damagePlayer));
        battleHUD.getDmgPlayerValLabel().setY(battleHUD.origDmgOpponentValLabelY);
//        battleShakeCam.startShaking();
        battleHUD.getDmgPlayerValLabel().setVisible(true);

        int hpVal = ProfileManager.getInstance().getProperty("currentPlayerHP", Integer.class);
        battleHUD.battleStatusUI.setHPValue(hpVal);

        if (hpVal <= 0) {
            GameScreen.setGameState(GameScreen.GameState.GAME_OVER);
        }
    }
}
