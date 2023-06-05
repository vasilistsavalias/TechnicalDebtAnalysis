package com.gdx.game.battle.generalEvents.specificEvents.BattleEvents;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.screen.BaseScreen;
import com.gdx.game.screen.GameOverScreen;
import com.gdx.game.screen.transition.effects.FadeOutTransitionEffect;
import com.gdx.game.screen.transition.effects.TransitionEffect;

import java.util.ArrayList;

import static com.gdx.game.battle.BattleHUD.LOGGER;

public class OpponentDefeatedEvent extends BattleEvent {

        public void activateEvent(Entity entity , BattleHUD battleHUD) {
          battleHUD.getDmgOpponentValLabel().setVisible(false);
         battleHUD.getDmgOpponentValLabel().setY(battleHUD.origDmgOpponentValLabelY);
          battleHUD.setOpponentDefeated();
            LOGGER.debug("Opponent is defeated");
            battleHUD.getDmgOpponentValLabel().setVisible(false);
            battleHUD.getDmgPlayerValLabel().setVisible(false);
            battleHUD.getBattleUI().setVisible(false);
            battleHUD.getBattleStatusUI().setVisible(false);


            ArrayList<TransitionEffect> effects = new ArrayList<>();
            effects.add(new FadeOutTransitionEffect(1f));


            int xpReward = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_XP_REWARD.toString()));
             battleHUD.battleStatusUI.addXPValue(xpReward);

            battleHUD.battleUI.setVisible(false);
            battleHUD.battleUI.setTouchable(Touchable.disabled);
            battleHUD.battleStatusUI.setVisible(false);
            battleHUD.battleConversation.notifBattleResume(battleHUD.enemy);

        }


    public void onNotify(Entity enemyEntity, BattleEvent event) {

    }


    public void onNotify(String value, ComponentEvent event) {

    }

    @Override
    public void onNotify(String value, InventoryEvent event) {

    }


    public void onNotify(int value, StatusEvent event) {

    }
}
