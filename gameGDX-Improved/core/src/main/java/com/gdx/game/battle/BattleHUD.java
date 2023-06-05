package com.gdx.game.battle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.animation.AnimatedImage;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEntity;
import com.gdx.game.battle.generalEvents.specificEvents.BattleEvents.BattleEvent;
import com.gdx.game.battle.generalEvents.specificEvents.ComponentEvents.ComponentEvent;
import com.gdx.game.battle.generalEvents.specificEvents.InventoryEvents.InventoryEvent;
import com.gdx.game.battle.generalEvents.specificEvents.StatusEvents.StatusEvent;
//import com.gdx.game.component.ComponentObserver;
import com.gdx.game.dialog.ConversationUI;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.entities.player.characterClass.ClassObserver;
import com.gdx.game.entities.player.characterClass.tree.Node;
import com.gdx.game.entities.player.characterClass.tree.Tree;
import com.gdx.game.inventory.InventoryItemLocation;
//import com.gdx.game.inventory.InventoryObserver;
import com.gdx.game.manager.ResourceManager;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.status.StatsUpUI;
//import com.gdx.game.status.StatusObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class BattleHUD implements Screen, BattleEntity, ClassObserver {

    public static  final Logger LOGGER = LoggerFactory.getLogger(BattleHUD.class);

    public Stage battleHUDStage;

    public BattleUI battleUI;
    public BattleStatusUI battleStatusUI;
    public static ConversationUI notificationUI;
    public BattleInventoryUI battleInventoryUI;
    public StatsUpUI statsUpUI;

    public Json json;
    public MapManager mapManager;
    public AnimatedImage playerImage;
    public com.gdx.game.entities.Entity player;
    public AnimatedImage opponentImage;
    public com.gdx.game.entities.Entity enemy;
    public BattleState battleState;
    public BattleConversation battleConversation;

    public final int enemyWidth = 50;
    public final int enemyHeight = 50;
    private final int playerWidth = 50;
    private final int playerHeight = 50;

    private float origDmgPlayerValLabelY = 0;
    public float origDmgOpponentValLabelY = 0;
    private Label dmgPlayerValLabel;
    private Label dmgOpponentValLabel;
    private Table dmgPlayerLabelTable = new Table();
    private Table dmgOpponentLabelTable = new Table();
    public Vector2 currentOpponentImagePosition = new Vector2(0,0);
    private Vector2 currentPlayerImagePosition = new Vector2(0,0);
    private boolean opponentDefeated = false;
    private float fadeTimeAlpha = 1;

    public BattleHUD(MapManager mapManager_, Stage battleStage, BattleState battleState_) {
        this.mapManager = mapManager_;
        this.battleState = battleState_;

        json = new Json();
        player = mapManager.getPlayer();
        battleConversation = new BattleConversation();

        battleState.addObserver(this);
//        battleConversation.addObserver(this);

        battleHUDStage = new Stage(battleStage.getViewport());
        playerImage = new AnimatedImage();
        playerImage.setTouchable(Touchable.disabled);
        enemy = EntityFactory.getInstance().getEntityByName(mapManager.getPlayer().getEntityEncounteredType());
        opponentImage = new AnimatedImage();
        opponentImage.setTouchable(Touchable.disabled);
        battleState.setPlayer(player);
        battleState.setCurrentOpponent(enemy);

        dmgPlayerValLabel = new Label("0", ResourceManager.skin);
        dmgPlayerValLabel.setVisible(false);
        origDmgPlayerValLabelY = dmgPlayerValLabel.getY() + playerHeight;
        dmgOpponentValLabel = new Label("0", ResourceManager.skin);
        dmgOpponentValLabel.setVisible(false);
        origDmgOpponentValLabelY = dmgOpponentValLabel.getY() + enemyHeight;

        battleStatusUI = new BattleStatusUI();
        battleStatusUI.setKeepWithinStage(false);
        battleStatusUI.setVisible(true);
        battleStatusUI.setPosition(0, 0);
        battleStatusUI.setWidth(battleStage.getWidth() / 2);
        battleStatusUI.setHeight(battleStage.getHeight() / 4);

        battleInventoryUI = new BattleInventoryUI();
        battleInventoryUI.setKeepWithinStage(false);
        battleInventoryUI.setMovable(true);
        battleInventoryUI.setVisible(false);
        battleInventoryUI.setPosition(battleStage.getWidth() / 4, battleStage.getHeight() / 4);
        Array<InventoryItemLocation> inventory = ProfileManager.getInstance().getProperty("playerInventory", Array.class);
        battleInventoryUI.fillInventory(inventory);


        battleUI = new BattleUI(battleState, battleInventoryUI);
        battleUI.setMovable(false);
        battleUI.setVisible(true);
        battleUI.setKeepWithinStage(false);
        battleUI.setPosition(battleStage.getWidth() / 10, 2 * battleStage.getHeight() / 3);
        battleUI.setWidth(battleStage.getWidth() / 2);
        battleUI.setHeight(battleStage.getHeight() / 4);

        notificationUI = new ConversationUI();
        notificationUI.removeActor(notificationUI.findActor("scrollPane"));
        notificationUI.getCloseButton().setVisible(false);
        notificationUI.getCloseButton().setTouchable(Touchable.disabled);
        notificationUI.setTitle("");
        notificationUI.setMovable(false);
        notificationUI.setVisible(false);
        notificationUI.setPosition(0, 0);
        notificationUI.setWidth(battleHUDStage.getWidth());
        notificationUI.setHeight(battleHUDStage.getHeight() / 5);
        notificationUI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                notificationUI.setVisible(false);
                battleState.resumeOver();
            }
        });

        battleUI.validate();
        battleStatusUI.validate();
        notificationUI.validate();
        battleInventoryUI.validate();

        dmgPlayerLabelTable.add(dmgPlayerValLabel).padLeft(playerWidth / 2).padBottom(playerHeight * 4);
        dmgPlayerLabelTable.setPosition(currentPlayerImagePosition.x, currentPlayerImagePosition.y);
        dmgOpponentLabelTable.add(dmgOpponentValLabel).padLeft(enemyWidth / 2).padBottom(enemyHeight * 4);
        dmgOpponentLabelTable.setPosition(currentOpponentImagePosition.x, currentOpponentImagePosition.y);

        battleHUDStage.addActor(playerImage);
        battleHUDStage.addActor(opponentImage);
        battleHUDStage.addActor(dmgPlayerLabelTable);
        battleHUDStage.addActor(dmgOpponentLabelTable);
        battleHUDStage.addActor(battleUI);
        battleHUDStage.addActor(battleStatusUI);
        battleHUDStage.addActor(notificationUI);
        battleHUDStage.addActor(battleInventoryUI);

        Array<Actor> actors = battleInventoryUI.getInventoryActors();
        for(Actor actor : actors) {
            battleHUDStage.addActor(actor);
        }

//        battleStatusUI.addObserver(this);
//        battleInventoryUI.addObserver(this);
    }

    @Override
    public void onNotify(com.gdx.game.entities.Entity entity, BattleEvent event) {
//        switch(event) {
//            case PLAYER_TURN_START:
//                LOGGER.debug("Player turn start");
//                battleUI.setVisible(false);
//                battleUI.setTouchable(Touchable.disabled);
//                break;
//            case PLAYER_ADDED:
//                playerImage.setEntity(entity);
//                playerImage.setCurrentAnimation(com.gdx.game.entities.Entity.AnimationType.WALK_RIGHT);
//                playerImage.setSize(playerWidth, playerHeight);
//                playerImage.setPosition(0, 200);
//                playerImage.addAction(Actions.moveTo(200, 200, 2));
//
//                currentPlayerImagePosition.set(((MoveToAction) playerImage.getActions().get(0)).getX(), playerImage.getY());
//                LOGGER.debug("Player added on battle map");
//                break;
//            case OPPONENT_ADDED:
//                opponentImage.setEntity(entity);
//                opponentImage.setCurrentAnimation(com.gdx.game.entities.Entity.AnimationType.IMMOBILE);
//                opponentImage.setSize(enemyWidth, enemyHeight);
//                opponentImage.setPosition(600, 200);
//
//                currentOpponentImagePosition.set(opponentImage.getX(), opponentImage.getY());
//                LOGGER.debug("Opponent added on battle map");
//                /*if( battleShakeCam == null ){
//                    battleShakeCam = new ShakeCamera(currentImagePosition.x, currentImagePosition.y, 30.0f);
//                }*/
//
//                //Gdx.app.debug(TAG, "Image position: " + _image.getX() + "," + _image.getY() );
//
//                //this.getTitleLabel().setText("Level " + battleState.getCurrentZoneLevel() + " " + entity.getEntityConfig().getEntityID());
//                break;
//            case PLAYER_HIT_DAMAGE:
//                int damagePlayer = Integer.parseInt(player.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
//                dmgPlayerValLabel.setText(String.valueOf(damagePlayer));
//                dmgPlayerValLabel.setY(origDmgPlayerValLabelY);
//                //battleShakeCam.startShaking();
//                dmgPlayerValLabel.setVisible(true);
//
//                int hpVal = ProfileManager.getInstance().getProperty("currentPlayerHP", Integer.class);
//                battleStatusUI.setHPValue(hpVal);
//
//                if(hpVal <= 0){
//                    GameScreen.setGameState(GameScreen.GameState.GAME_OVER);
//                }
//                break;
//            case OPPONENT_HIT_DAMAGE:
//                int damageEnemy = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_HIT_DAMAGE_TOTAL.toString()));
//                dmgOpponentValLabel.setText(String.valueOf(damageEnemy));
//                dmgOpponentValLabel.setY(origDmgOpponentValLabelY);
//                //battleShakeCam.startShaking();
//                dmgOpponentValLabel.setVisible(true);
//                LOGGER.debug("Opponent deals " + damageEnemy + " damages");
//                break;
//            case OPPONENT_DEFEATED:
//                dmgOpponentValLabel.setVisible(false);
//                dmgOpponentValLabel.setY(origDmgOpponentValLabelY);
//                setOpponentDefeated();
//                LOGGER.debug("Opponent is defeated");
//
//                int xpReward = Integer.parseInt(entity.getEntityConfig().getPropertyValue(EntityConfig.EntityProperties.ENTITY_XP_REWARD.toString()));
//                battleStatusUI.addXPValue(xpReward);
//
//                battleUI.setVisible(false);
//                battleUI.setTouchable(Touchable.disabled);
//                battleStatusUI.setVisible(false);
//                battleConversation.notifBattleResume(enemy);
//                break;
//            case OPPONENT_TURN_DONE:
//                if(GameScreen.getGameState() != GameScreen.GameState.GAME_OVER) {
//                    battleUI.setVisible(true);
//                    battleUI.setTouchable(Touchable.enabled);
//                }
//                LOGGER.debug("Opponent turn done");
//                break;
//            case PLAYER_TURN_DONE:
//                battleState.opponentAttacks();
//                LOGGER.debug("Player turn done");
//                break;
//            /*case PLAYER_USED_MAGIC:
//                float x = currentImagePosition.x + (enemyWidth/2);
//                float y = currentImagePosition.y + (enemyHeight/2);
//                //effects.add(ParticleEffectFactory.getParticleEffect(ParticleEffectFactory.ParticleEffectType.WAND_ATTACK, x,y));
//                break;*/
//            default:
//                break;
//        }
//    }
        event.activateEvent(entity, this);
    }

   @Override
    public void onNotify(String value, ComponentEvent event) {
//        switch(event) {
//            case LOAD_RESUME:
//                EntityConfig config = json.fromJson(EntityConfig.class, value);
//                notificationUI.loadResume(config);
//                break;
//            case SHOW_RESUME:
//                EntityConfig configShow = json.fromJson(EntityConfig.class, value);
//
//                if (configShow.getEntityID().equalsIgnoreCase(notificationUI.getCurrentEntityID())) {
//                    notificationUI.setVisible(true);
//                }
//                break;
//            default:
//                break;
//        }
        event.activateEvent(value,this);
    }

    @Override
    public void onNotify(String value, InventoryEvent event) {
        event.activateEvent(value,this);
    }

    @Override
    public void onNotify(int value, StatusEvent event) {
//        switch(event) {
//            case UPDATED_HP:
//                ProfileManager.getInstance().setProperty("currentPlayerHP", battleStatusUI.getHPValue());
//                break;
//            case UPDATED_LEVEL:
//                ProfileManager.getInstance().setProperty("currentPlayerLevel", battleStatusUI.getLevelValue());
//                createStatsUpUI(battleStatusUI.getNbrLevelUp());
//                break;
//            case UPDATED_MP:
//                ProfileManager.getInstance().setProperty("currentPlayerMP", battleStatusUI.getMPValue());
//                break;
//            case UPDATED_XP:
//                ProfileManager.getInstance().setProperty("currentPlayerXP", battleStatusUI.getXPValue());
//                break;
//            case LEVELED_UP:
//                //notify(AudioObserver.AudioCommand.MUSIC_PLAY_ONCE, AudioObserver.AudioTypeEvent.MUSIC_LEVEL_UP_FANFARE);
//                break;
//            default:
//                break;
//        }
        event.activateEvent(value, this);
    }

    @Override
    public void onNotify(String value, ClassEvent event) {
        if (Objects.requireNonNull(event) == ClassEvent.CHECK_UPGRADE_TREE_CLASS) {
            String currentClass = ProfileManager.getInstance().getProperty("characterClass", String.class);
            int AP = ProfileManager.getInstance().getProperty("currentPlayerCharacterAP", Integer.class);
            int DP = ProfileManager.getInstance().getProperty("currentPlayerCharacterDP", Integer.class);
            String configFilePath = player.getEntityConfig().getClassTreePath();
            Tree tree = Tree.buildClassTree(configFilePath);
            Node node = tree.checkForClassUpgrade(currentClass, AP, DP);
            Tree.saveNewClass(node);

            if (node != null) {
                notificationUI.loadUpgradeClass(node.getClassId());
            }
        }
    }

    public void createStatsUpUI(int nbrLevelUp) {
        statsUpUI = new StatsUpUI(nbrLevelUp);
        statsUpUI.setPosition(battleHUDStage.getWidth() / 4, battleHUDStage.getHeight() / 4);
        statsUpUI.setKeepWithinStage(false);
        statsUpUI.setWidth(battleHUDStage.getWidth() / 2);
        statsUpUI.setHeight(battleHUDStage.getHeight() / 2);
        statsUpUI.setMovable(false);

        statsUpUI.validate();
        statsUpUI.addObserver((ClassObserver) this);
        battleHUDStage.addActor(statsUpUI);
    }

    public Stage getBattleHUDStage() {
        return battleHUDStage;
    }

    public BattleUI getBattleUI() {
        return battleUI;
    }

    public BattleStatusUI getBattleStatusUI() {
        return battleStatusUI;
    }

    public BattleInventoryUI getBattleInventoryUI() {
        return battleInventoryUI;
    }

    public Label getDmgPlayerValLabel() {
        return dmgPlayerValLabel;
    }

    public Label getDmgOpponentValLabel() {
        return dmgOpponentValLabel;
    }

    public void setOpponentDefeated() {
        this.opponentDefeated = true;
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        battleHUDStage.act(delta);
        battleHUDStage.draw();

        if(dmgPlayerValLabel.isVisible() && dmgPlayerValLabel.getY() < this.battleHUDStage.getHeight()) {
            dmgPlayerValLabel.setY(dmgPlayerValLabel.getY()+5);
        }
        if(dmgOpponentValLabel.isVisible() && dmgOpponentValLabel.getY() < this.battleHUDStage.getHeight()) {
            dmgOpponentValLabel.setY(dmgOpponentValLabel.getY()+5);
        }

        if (playerImage.getActions().size == 0 && !playerImage.getCurrentAnimationType().equals(com.gdx.game.entities.Entity.AnimationType.LOOK_RIGHT)) {
            playerImage.setCurrentAnimation(com.gdx.game.entities.Entity.AnimationType.LOOK_RIGHT);
        }

        if (opponentDefeated) {
            fadeTimeAlpha -= (1f / 60f) / 2;
            opponentImage.setColor(1.0f, 1.0f, 1.0f, fadeTimeAlpha);
        }
    }

    @Override
    public void resize(int width, int height) {
        battleHUDStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        battleHUDStage.dispose();
        player.dispose();
        enemy.dispose();
        battleUI.remove();
        battleStatusUI.remove();
        notificationUI.remove();
        battleInventoryUI.remove();
        playerImage.remove();
        opponentImage.remove();
        dmgPlayerLabelTable.remove();
        dmgOpponentLabelTable.remove();
        dmgOpponentValLabel.remove();
        dmgPlayerValLabel.remove();
    }
}
