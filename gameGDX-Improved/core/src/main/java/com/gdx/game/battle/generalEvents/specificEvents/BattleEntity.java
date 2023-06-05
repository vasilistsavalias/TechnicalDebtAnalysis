package com.gdx.game.battle.generalEvents.specificEvents;

import com.gdx.game.battle.generalEvents.GeneralEntity;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEvents.*;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;

public interface BattleEntity extends GeneralEntity {
//    enum BattleEvent {
//        OPPONENT_ADDED,
//        OPPONENT_HIT_DAMAGE,
//        OPPONENT_DEFEATED,
//        OPPONENT_TURN_DONE,
//        PLAYER_ADDED,
//        PLAYER_HIT_DAMAGE,
//        PLAYER_RUNNING,
//        PLAYER_TURN_DONE,
//        PLAYER_TURN_START,
//        PLAYER_USED_MAGIC,
//        RESUME_OVER,
//        NONE
//    }

    BattleEvent opponentAdded = new OpponentAddedEvent();
    BattleEvent opponentHitDamage = new OpponentHitDamageEvent();
    BattleEvent opponentDefeated = new OpponentDefeatedEvent();
    BattleEvent opponentTurnDone = new OpponentTurnDoneEvent();
    BattleEvent playerAdded = new PlayerAddedEvent();
    BattleEvent playerHitDamaged = new PlayerHitDamageEvent();
    BattleEvent playerRunning = new PlayerRunningEvent();
    BattleEvent playerTurnDone = new PlayerTurnDoneEvent();
    BattleEvent playerTurnStart = new PlayerTurnStartEvent();
    BattleEvent playerUsedMagic = new PlayerUsedMagicEvent();
    BattleEvent ResumeOver = new ResumeOverEvent();
    BattleEvent None = new NoneBattleEvent();



    void onNotify(final com.gdx.game.entities.Entity enemyEntity, BattleEvent event);

    void onNotify(String value, ComponentEvent event);

    void onNotify(String value, InventoryEvent event);

    void onNotify(int value, StatusEvent event);
}
