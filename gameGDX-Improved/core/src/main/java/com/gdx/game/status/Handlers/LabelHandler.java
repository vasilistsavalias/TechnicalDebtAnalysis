package com.gdx.game.status.Handlers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.gdx.game.status.StatusUI;

import static com.gdx.game.manager.ResourceManager.STATUS_UI_SKIN;

public abstract class LabelHandler implements Handler {

    public static void handleLevel(StatusUI statusUI) {
        Label levelLabel = new Label(" lv: ", STATUS_UI_SKIN);
        statusUI.updateLevelValLabel();
        statusUI.addAndAlign(levelLabel, Align.left);
        statusUI.addAndAlign(statusUI.getLevelValLabel(), Align.left);
    }
    public static void handleGold(StatusUI statusUI) {
        Label goldLabel = new Label(" gp: ", STATUS_UI_SKIN);
        statusUI.updateGoldValLabel();
        statusUI.add(goldLabel);
        statusUI.addAndAlign(statusUI.getGoldValLabel(), Align.left);
    }

}
