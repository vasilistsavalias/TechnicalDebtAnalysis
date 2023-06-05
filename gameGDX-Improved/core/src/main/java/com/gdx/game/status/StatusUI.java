package com.gdx.game.status;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.status.Handlers.BarHandler;
import com.gdx.game.status.Handlers.ButtonHandler;
import com.gdx.game.status.Handlers.LabelHandler;

import static com.gdx.game.manager.ResourceManager.STATUS_UI_SKIN;

public class StatusUI extends Window implements StatusSubject {
    private Image hpBar;
    private Image mpBar;
    private Image xpBar;

    private ImageButton inventoryButton;
    private ImageButton questButton;
    private Array<StatusObserver> observers;

    private Array<LevelTable> levelTables;
    private static final String LEVEL_TABLE_CONFIG = "scripts/level_tables.json";

    //Attributes
    private int levelVal = -1;
    private int goldVal = -1;
    private int hpVal = -1;
    private int mpVal = -1;
    private int xpVal = 0;

    private int xpCurrentMax = -1;
    private int hpCurrentMax = -1;
    private int mpCurrentMax = -1;

    private Label hpValLabel;
    private Label hpValLabelMax;
    private Label mpValLabel;
    private Label mpValLabelMax;
    private Label xpValLabel;
    private Label levelValLabel;
    private Label goldValLabel;

    private float barWidth = 0;
    private float barHeight = 0;
    private int nbrLevelUp = 0;

    public StatusUI(){
        super("stats", STATUS_UI_SKIN);

        levelTables = LevelTable.getLevelTables(LEVEL_TABLE_CONFIG);

        observers = new Array<>();

        //Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add();
        ButtonHandler.handleQuest(this);
        ButtonHandler.handleInventory(this);
        this.row();
        BarHandler.handleHP(this);
        BarHandler.handleMP(this);
        BarHandler.handleXP(this);
        LabelHandler.handleLevel(this);
        this.row();
        LabelHandler.handleGold(this);
        //this.debug();
        this.pack();

        barWidth = hpBar.getWidth();
        barHeight = hpBar.getHeight();
    }
    public void updateLevelValLabel() {
        levelValLabel = new Label(String.valueOf(levelVal), STATUS_UI_SKIN);
    }
    public void updateGoldValLabel() {
        goldValLabel = new Label(String.valueOf(goldVal), STATUS_UI_SKIN);
    }

    public void addAndAlign(Actor actor, int alignment) {
        this.add(actor).align(alignment);
    }

    public ImageButton getInventoryButton() {
        return inventoryButton;
    }

    public ImageButton getQuestButton() {
        return questButton;
    }

    public int getLevelValue() {
        return levelVal;
    }

    public void setLevelValue(int levelValue, boolean isFromQuest) {
        this.levelVal = levelValue;
        levelValLabel.setText(String.valueOf(levelVal));
        if(isFromQuest) {
            notify(levelVal, StatusObserver.StatusEvent.UPDATED_LEVEL_FROM_QUEST);
        } else {
            notify(levelVal, StatusObserver.StatusEvent.UPDATED_LEVEL);
        }
    }

    public int getNbrLevelUp() {
        return nbrLevelUp;
    }

    public void setNbrLevelUp(int nbrLevelUp) {
        this.nbrLevelUp = nbrLevelUp;
    }

    public int getGoldValue() {
        return goldVal;
    }

    public void setGoldValue(int goldValue) {
        this.goldVal = goldValue;
        goldValLabel.setText(String.valueOf(goldVal));
        notify(goldVal, StatusObserver.StatusEvent.UPDATED_GP);
    }

    public void addGoldValue(int goldValue) {
        this.goldVal += goldValue;
        goldValLabel.setText(String.valueOf(goldVal));
        notify(goldVal, StatusObserver.StatusEvent.UPDATED_GP);
    }

    public int getXPValue() {
        return xpVal;
    }

    public void addXPValue(int xpValue, boolean isFromQuest) {
        this.xpVal += xpValue;

        if(xpVal > xpCurrentMax) {
            updateToNewLevel(isFromQuest);
        }

        xpValLabel.setText(String.valueOf(xpVal));

        updateBar(xpBar, xpVal, xpCurrentMax);

        notify(xpVal, StatusObserver.StatusEvent.UPDATED_XP);
    }

    public void setXPValue(int xpValue) {
        this.xpVal = xpValue;

        if(xpVal > xpCurrentMax) {
            updateToNewLevel(false);
        }

        xpValLabel.setText(String.valueOf(xpVal));

        updateBar(xpBar, xpVal, xpCurrentMax);

        notify(xpVal, StatusObserver.StatusEvent.UPDATED_XP);
    }

    public void setXPValueMax(int maxXPValue) {
        this.xpCurrentMax = maxXPValue;
    }

    public void setStatusForLevel(int level) {
        for(LevelTable table: levelTables) {
            if(Integer.parseInt(table.getLevelID()) == level) {
                setXPValueMax(table.getXpMax());
                setXPValue(0);

                setHPValueMax(table.getHpMax());
                setHPValue(table.getHpMax());

                setMPValueMax(table.getMpMax());
                setMPValue(table.getMpMax());

                setLevelValue(Integer.parseInt(table.getLevelID()), false);
                return;
            }
        }
    }

    public void updateToNewLevel(boolean isFromQuest){
        int xpRemain = xpVal;
        for(LevelTable table: levelTables) {
            if(xpRemain <= table.getXpMax()) {
                setXPValueMax(table.getXpMax());
                setXPValue(xpRemain);

                setHPValueMax(table.getHpMax());
                setHPValue(table.getHpMax());

                setMPValueMax(table.getMpMax());
                setMPValue(table.getMpMax());

                setNbrLevelUp(Integer.parseInt(table.getLevelID()) - levelVal);
                setLevelValue(Integer.parseInt(table.getLevelID()), isFromQuest);
                notify(levelVal, StatusObserver.StatusEvent.LEVELED_UP);
                return;
            }
            if(levelVal <= Integer.parseInt(table.getLevelID())) {
                xpRemain = xpRemain - table.getXpMax();
            }
        }
    }

    public int getXPValueMax() {
        return xpCurrentMax;
    }

    //HP
    public int getHPValue() {
        return hpVal;
    }

    public void removeHPValue(int hpValue) {
        hpVal = MathUtils.clamp(hpVal - hpValue, 0, hpCurrentMax);
        hpValLabel.setText(String.valueOf(hpVal));

        updateBar(hpBar, hpVal, hpCurrentMax);

        notify(hpVal, StatusObserver.StatusEvent.UPDATED_HP);
    }

    public void addHPValue(int hpValue) {
        hpVal = MathUtils.clamp(hpVal + hpValue, 0, hpCurrentMax);
        hpValLabel.setText(String.valueOf(hpVal));

        updateBar(hpBar, hpVal, hpCurrentMax);

        notify(hpVal, StatusObserver.StatusEvent.UPDATED_HP);
    }

    public void setHPValue(int hpValue) {
        this.hpVal = hpValue;
        hpValLabel.setText(String.valueOf(hpVal));

        updateBar(hpBar, hpVal, hpCurrentMax);

        notify(hpVal, StatusObserver.StatusEvent.UPDATED_HP);
    }

    public void setHPValueMax(int maxHPValue) {
        this.hpCurrentMax = maxHPValue;
        hpValLabelMax.setText(String.valueOf(hpCurrentMax));
    }

    public int getHPValueMax() {
        return hpCurrentMax;
    }

    //MP
    public int getMPValue() {
        return mpVal;
    }

    public void removeMPValue(int mpValue) {
        mpVal = MathUtils.clamp(mpVal - mpValue, 0, mpCurrentMax);
        mpValLabel.setText(String.valueOf(mpVal));

        updateBar(mpBar, mpVal, mpCurrentMax);

        notify(mpVal, StatusObserver.StatusEvent.UPDATED_MP);
    }

    public void addMPValue(int mpValue) {
        mpVal = MathUtils.clamp(mpVal + mpValue, 0, mpCurrentMax);
        mpValLabel.setText(String.valueOf(mpVal));

        updateBar(mpBar, mpVal, mpCurrentMax);

        notify(mpVal, StatusObserver.StatusEvent.UPDATED_MP);
    }

    public void setMPValue(int mpValue) {
        this.mpVal = mpValue;
        mpValLabel.setText(String.valueOf(mpVal));

        updateBar(mpBar, mpVal, mpCurrentMax);

        notify(mpVal, StatusObserver.StatusEvent.UPDATED_MP);
    }

    public void setMPValueMax(int maxMPValue) {
        this.mpCurrentMax = maxMPValue;
        mpValLabelMax.setText(String.valueOf(mpCurrentMax));
    }

    public int getMPValueMax() {
        return mpCurrentMax;
    }

    public void updateBar(Image bar, int currentVal, int maxVal) {
        int val = MathUtils.clamp(currentVal, 0, maxVal);
        float tempPercent = (float) val / (float) maxVal;
        float percentage = MathUtils.clamp(tempPercent, 0, 100);
        bar.setSize(barWidth *percentage, barHeight);
    }

    @Override
    public void addObserver(StatusObserver statusObserver) {
        observers.add(statusObserver);
    }

    @Override
    public void removeObserver(StatusObserver statusObserver) {
        observers.removeValue(statusObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for(StatusObserver observer: observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(int value, StatusObserver.StatusEvent event) {
        for(StatusObserver observer: observers) {
            observer.onNotify(value, event);
        }
    }

    public void addHPToGroup() {
        Label hpLabel = new Label(" hp: ", STATUS_UI_SKIN);
        Label separator = new Label("/", STATUS_UI_SKIN);
        this.add(hpLabel);
        this.add(hpValLabel);
        this.add(separator);
        this.add(hpValLabelMax);
        this.row();
    }

    public void setHPBar(Image hpBar) {
        this.hpBar = hpBar;
    }

    public void setHPBarPosition(int x, int y) {
        this.hpBar.setPosition(x, y);
    }

    public void updateHPValLabel() {
        this.hpValLabel = new Label(String.valueOf(hpVal), STATUS_UI_SKIN);
    }

    public void updateHPValMaxLabel() {
        this.hpValLabelMax = new Label(String.valueOf(hpCurrentMax), STATUS_UI_SKIN);
    }

    public Actor getHPBar() {
        return this.hpBar;
    }

    public void setMPBar(Image mpBar) {
        this.mpBar = mpBar;
    }

    public void setMPBarPosition(int x, int y) {
        this.mpBar.setPosition(x,y);
    }

    public void updateMPValLabel() {
        this.mpValLabel = new Label(String.valueOf(mpVal), STATUS_UI_SKIN);
    }

    public void updateMPValMaxLabel() {
        this.mpValLabelMax = new Label(String.valueOf(mpCurrentMax), STATUS_UI_SKIN);
    }

    public void addMPToGroup() {
        Label mpLabel = new Label(" mp: ", STATUS_UI_SKIN);
        Label separator = new Label("/", STATUS_UI_SKIN);
        this.add(mpLabel);
        this.add(mpValLabel);
        this.add(separator);
        this.add(mpValLabelMax);
        this.row();

    }

    public Actor getMPBar() {
        return this.mpBar;
    }

    public void setXPBar(Image xpBar) {
        this.xpBar = xpBar;
    }

    public void setXPBarPosition(int x, int y) {
        this.xpBar.setPosition(x, y);
    }

    public void updateXPValLabel() {
        this.xpValLabel = new Label(String.valueOf(xpVal), STATUS_UI_SKIN);
    }

    public Actor getXPBar() {
        return this.xpBar;
    }

    public void addXPToGroup() {
        Label xpLabel = new Label(" xp: ", STATUS_UI_SKIN);
        this.add(xpLabel);
        this.add(xpValLabel).align(Align.left).padRight(20);
        this.row();
    }

    public Label getLevelValLabel() {
        return this.levelValLabel;
    }

    public Label getGoldValLabel() {
        return this.goldValLabel;
    }

    public void setInventoryButton(ImageButton imageButton) {
        this.inventoryButton = imageButton;
    }

    public void setInventoryButtonSize(int x, int y) {
        this.inventoryButton.getImageCell().size(x, y);
    }

    public void setQuestButton(ImageButton imageButton) {
        this.questButton = imageButton;
    }

    public void setQuestButtonSize(int x, int y) {
        this.questButton.getImageCell().size(x, y);
    }
}
