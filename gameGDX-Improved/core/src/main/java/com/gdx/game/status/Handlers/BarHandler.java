package com.gdx.game.status.Handlers;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.gdx.game.status.StatusUI;

import static com.gdx.game.manager.ResourceManager.STATUS_UI_SKIN;
import static com.gdx.game.manager.ResourceManager.STATUS_UI_TEXTURE_ATLAS;

public abstract class BarHandler implements Handler {
    public static void handleHP(StatusUI statusUI) {
        statusUI.setHPBar(new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("HP_Bar")));
        statusUI.setHPBarPosition(3, 6);
        statusUI.updateHPValLabel();
        statusUI.updateHPValMaxLabel();

        Image bar = new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("Bar"));
        WidgetGroup group = new WidgetGroup(bar, statusUI.getHPBar());
        statusUI.add(group).size(bar.getWidth(), bar.getHeight()).padRight(10);

        statusUI.addHPToGroup();
    }

    public static void handleMP(StatusUI statusUI) {
        statusUI.setMPBar(new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("MP_Bar")));
        statusUI.setMPBarPosition(3, 6);
        statusUI.updateMPValLabel();
        statusUI.updateMPValMaxLabel();

        Image bar = new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("Bar"));
        WidgetGroup group = new WidgetGroup(bar, statusUI.getMPBar());
        statusUI.add(group).size(bar.getWidth(), bar.getHeight()).padRight(10);

        statusUI.addMPToGroup();
    }

    public static void handleXP(StatusUI statusUI) {
        statusUI.setXPBar(new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("XP_Bar")));
        statusUI.setXPBarPosition(3, 6);
        statusUI.updateXPValLabel();

        Image bar = new Image(STATUS_UI_TEXTURE_ATLAS.findRegion("Bar"));
        WidgetGroup group = new WidgetGroup(bar, statusUI.getXPBar());
        statusUI.add(group).size(bar.getWidth(), bar.getHeight()).padRight(10);

        statusUI.addXPToGroup();
    }
}
