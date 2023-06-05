package com.gdx.game.quest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;

import java.util.ArrayList;

import static com.gdx.game.quest.QuestTask.QuestType.FETCH;

public interface Handler {
    enum Action {
        UPDATE,
        INIT
    }
    static void RunRutime(MapManager mapMgr, QuestGraph graph, Action action) {
        ArrayList<QuestTask> allQuestTasks = graph.getAllQuestTasks();
        for(QuestTask questTask: allQuestTasks) {
            if (questTask.isTaskComplete()) {
                continue;
            }
            //We first want to make sure the task is available and is relevant to current location
            if (!graph.isQuestTaskAvailable(questTask.getId())) {
                continue;
            }
            String taskLocation = questTask.getPropertyValue(QuestTask.QuestTaskPropertyType.TARGET_LOCATION.toString());
            if (taskLocation == null || taskLocation.isEmpty() || !taskLocation.equalsIgnoreCase(mapMgr.getCurrentMapType().toString())) {
                continue;
            }
            switch (action) {
                case UPDATE:
                    if (questTask.getQuestType() == FETCH) {
                        String taskConfig = questTask.getPropertyValue(QuestTask.QuestTaskPropertyType.TARGET_TYPE.toString());
                        if (taskConfig == null || taskConfig.isEmpty()) {
                            break;
                        }
                        EntityConfig config = Entity.getEntityConfig(taskConfig);

                        Array<Vector2> questItemPositions = ProfileManager.getInstance().getProperty(config.getEntityID(), Array.class);
                        if (questItemPositions == null) {
                            break;
                        }

                        //Case where all the items have been picked up
                        if (questItemPositions.size == 0) {
                            questTask.setTaskComplete();
                            graph.printLOGGER("TASK : " + questTask.getId() + " is complete of Quest: " + graph.getQuestID());
                            graph.printLOGGER("INFO : " + QuestTask.QuestTaskPropertyType.TARGET_TYPE.toString());
                        }
                    }
                break;
                case INIT:
                    if (questTask.getQuestType() == FETCH) {
                        Array<Entity> questEntities = new Array<>();
                        Array<Vector2> positions = mapMgr.getQuestItemSpawnPositions(graph.getQuestID(), questTask.getId());
                        String taskConfig = questTask.getPropertyValue(QuestTask.QuestTaskPropertyType.TARGET_TYPE.toString());
                        if(taskConfig == null || taskConfig.isEmpty()) {
                            break;
                        }
                        EntityConfig config = Entity.getEntityConfig(taskConfig);

                        Array<Vector2> questItemPositions = ProfileManager.getInstance().getProperty(config.getEntityID(), Array.class);

                        if(questItemPositions == null) {
                            questItemPositions = new Array<>();
                            for(Vector2 position: positions) {
                                questItemPositions.add(position);
                                Entity entity = Entity.initEntity(config, position);
                                entity.getEntityConfig().setCurrentQuestID(graph.getQuestID());
                                questEntities.add(entity);
                            }
                        } else {
                            for(Vector2 questItemPosition: questItemPositions) {
                                Entity entity = Entity.initEntity(config, questItemPosition);
                                entity.getEntityConfig().setCurrentQuestID(graph.getQuestID());
                                questEntities.add(entity);
                            }
                        }

                        mapMgr.addMapQuestEntities(questEntities);
                        ProfileManager.getInstance().setProperty(config.getEntityID(), questItemPositions);
                    }
                break;
                default:
                break;
            }
        }
    }
}
