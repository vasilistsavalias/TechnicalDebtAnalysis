package com.gdx.game.status.Handlers;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;
import com.gdx.game.status.StatusUI;

import static com.gdx.game.manager.ResourceManager.STATUS_UI_SKIN;

public abstract class ButtonHandler implements Handler {

    public static void handleInventory(StatusUI statusUI) {
        statusUI.setInventoryButton(new ImageButton(STATUS_UI_SKIN, "inventory-button"));
        statusUI.setInventoryButtonSize(32, 32);
        statusUI.addAndAlign(statusUI.getInventoryButton(), Align.right);
    }
    public static void handleQuest(StatusUI statusUI) {
        statusUI.setQuestButton(new ImageButton(STATUS_UI_SKIN, "quest-button"));
        statusUI.setQuestButtonSize(32, 32);
        statusUI.addAndAlign(statusUI.getQuestButton(), Align.center);
    }

}
