package com.gdx.game.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.profile.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleState extends BattleSubject {
    private static final Logger LOGGER = LoggerFactory.getLogger(BattleState.class);

    private com.gdx.game.entities.Entity currentOpponent;
    private com.gdx.game.entities.Entity player;
    private int currentZoneLevel = 0;
    private int currentPlayerAP;
    private int currentPlayerDP;
    private int currentPlayerWandAPPoints = 0;
    private final int chanceOfAttack = 25;
    private final int chanceOfEscape = 40;
    private final int criticalChance = 90;
    private Timer.Task playerAttackCalculations;
    private Timer.Task opponentAttackCalculations;
    private Timer.Task checkPlayerMagicUse;

    public BattleState() {
        playerAttackCalculations = getPlayerAttackCalculationTimer();
        opponentAttackCalculations = getOpponentAttackCalculationTimer();
        checkPlayerMagicUse = getPlayerMagicUseCheckTimer();

        currentPlayerAP = ProfileManager.getInstance().getProperty("currentPlayerAP", Integer.class);
        currentPlayerDP = ProfileManager.getInstance().getProperty("currentPlayerDP", Integer.class);
    }

    public void resetDefaults() {
        LOGGER.debug("Resetting defaults...");
        currentZoneLevel = 0;
        currentPlayerAP = 0;
        currentPlayerDP = 0;
        currentPlayerWandAPPoints = 0;
        playerAttackCalculations.cancel();
        opponentAttackCalculations.cancel();
        checkPlayerMagicUse.cancel();
    }

    public void setCurrentZoneLevel(int zoneLevel) {
        currentZoneLevel = zoneLevel;
    }

    public int getCurrentZoneLevel() {
        return currentZoneLevel;
    }

    public boolean isOpponentReady() {
        if(currentZoneLevel == 0) {
            return false;
        }
        int randomVal = MathUtils.random(1,100);

        //Gdx.app.debug(TAG, "CHANGE OF ATTACK: " + _chanceOfAttack + " randomval: " + randomVal);

        if(chanceOfAttack > randomVal) {
            //setCurrentOpponent();
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentOpponent(com.gdx.game.entities.Entity entity) {
        LOGGER.debug("Entered BATTLE ZONE: " + currentZoneLevel);
        //BattleEntity entity = MonsterFactory.getInstance().getRandomMonster(currentZoneLevel);
        if(entity == null) {
            return;
        }
        this.currentOpponent = entity;
        notify(entity, BattleEntity.opponentAdded);
    }

    public void setPlayer(com.gdx.game.entities.Entity entity) {
        if(entity == null) {
            return;
        }
        this.player = entity;
        notify(entity, BattleEntity.playerAdded);
    }

    public void playerAttacks() {
        if(currentOpponent == null) {
            return;
        }

        //Check for magic if used in attack; If we don't have enough MP, then return
        int mpVal = ProfileManager.getInstance().getProperty("currentPlayerMP", Integer.class);
        notify(currentOpponent, BattleEntity.playerTurnStart);

        if(currentPlayerWandAPPoints == 0) {
            if(!playerAttackCalculations.isScheduled()) {
                Timer.schedule(playerAttackCalculations, 1);
            }
        } else if(currentPlayerWandAPPoints > mpVal) {
            notify(currentOpponent, BattleEntity.playerTurnDone);
        } else {
            if(!checkPlayerMagicUse.isScheduled() && !playerAttackCalculations.isScheduled()) {
                Timer.schedule(checkPlayerMagicUse, .5f);
                Timer.schedule(playerAttackCalculations, 1);
            }
        }
    }

    public void opponentAttacks() {
        if(currentOpponent == null) {
            return;
        }

        int currentOpponentHP = Integer.parseInt(currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HEALTH_POINTS.toString()));
        if(!opponentAttackCalculations.isScheduled() && currentOpponentHP > 0) {
            Timer.schedule(opponentAttackCalculations, 1);
        }
    }

    public void resumeOver() {
        notify(currentOpponent, BattleEntity.ResumeOver);
    }

    private Timer.Task getPlayerMagicUseCheckTimer() {
        return new Timer.Task() {
            @Override
            public void run() {
                int mpVal = ProfileManager.getInstance().getProperty("currentPlayerMP", Integer.class);
                mpVal -= currentPlayerWandAPPoints;
                ProfileManager.getInstance().setProperty("currentPlayerMP", mpVal);
                BattleState.this.notify(currentOpponent, BattleEntity.playerUsedMagic);
            }
        };
    }

    private Timer.Task getPlayerAttackCalculationTimer() {
        return new Timer.Task() {
            @Override
            public void run() {
                int currentOpponentHP = Integer.parseInt(currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HEALTH_POINTS.toString()));
                int currentOpponentDP = Integer.parseInt(currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_DEFENSE_POINTS.toString()));

                int damage = MathUtils.clamp(currentPlayerAP - currentOpponentDP, 0, currentPlayerAP);

                LOGGER.debug("ENEMY HAS " + currentOpponentHP + " hit with damage: " + damage);

                currentOpponentHP = MathUtils.clamp(currentOpponentHP - damage, 0, currentOpponentHP);
                currentOpponent.getEntityConfig().setPropertyValue(EntityConfig.EntityProperties.ENTITY_HEALTH_POINTS.toString(), String.valueOf(currentOpponentHP));

                LOGGER.debug("Player attacks " + currentOpponent.getEntityConfig().getEntityID() + " leaving it with HP: " + currentOpponentHP);

                currentOpponent.getEntityConfig().setPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString(), String.valueOf(damage));
                if(damage > 0) {
                    BattleState.this.notify(currentOpponent, BattleEntity.opponentHitDamage);
                }

                if(currentOpponentHP == 0) {
                    BattleState.this.notify(currentOpponent, BattleEntity.opponentDefeated);
                }

                BattleState.this.notify(currentOpponent, BattleEntity.playerTurnDone);
            }
        };
    }

    private Timer.Task getOpponentAttackCalculationTimer() {
        return new Timer.Task() {
            @Override
            public void run() {
                int currentOpponentAP = Integer.parseInt(currentOpponent.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_ATTACK_POINTS.toString()));
                int damage = MathUtils.clamp(currentOpponentAP - currentPlayerDP, 0, currentOpponentAP);
                int hpVal = ProfileManager.getInstance().getProperty("currentPlayerHP", Integer.class);
                hpVal = MathUtils.clamp( hpVal - damage, 0, hpVal);
                player.getEntityConfig().setPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString(), String.valueOf(damage));
                ProfileManager.getInstance().setProperty("currentPlayerHP", hpVal);

                if(damage > 0) {
                    BattleState.this.notify(currentOpponent, BattleEntity.playerHitDamaged);
                }

                LOGGER.debug("Player HIT for " + damage + " BY " + currentOpponent.getEntityConfig().getEntityID() + " leaving player with HP: " + hpVal);

                BattleState.this.notify(currentOpponent, BattleEntity.opponentTurnDone);
            }
        };
    }

    public void playerRuns() {
        int randomVal = MathUtils.random(1,100);
        if(chanceOfEscape > randomVal) {
            notify(currentOpponent, BattleEntity.playerRunning);
        } else if(randomVal > criticalChance) {
            opponentAttacks();
        }
    }
}
