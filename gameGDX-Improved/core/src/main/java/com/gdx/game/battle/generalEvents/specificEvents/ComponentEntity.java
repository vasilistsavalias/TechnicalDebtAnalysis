package com.gdx.game.battle.generalEvents.specificEvents;

import com.gdx.game.battle.generalEvents.GeneralEntity;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.*;

import javax.swing.*;

public interface ComponentEntity extends GeneralEntity {
    ComponentEvent loadConversation = new LoadConversationEvent();
    ComponentEvent showConversation = new ShowConversationEvent();
    ComponentEvent hideConversation = new HideConversationEvent();
    ComponentEvent loadResume = new LoadResumeEvent();
    ComponentEvent showResume = new ShowResumeEvent();
    ComponentEvent questLocationDiscovered = new QuestLocationDiscoveredEvent();

    ComponentEvent enemySpawnLocationChanged = new EnemySpawnLocationChangedEvent();
    ComponentEvent startBattle = new StartBattleEvent();
    ComponentEvent optionInput = new optionInputEvent();


    void onNotify(String value, ComponentEvent event);
}
