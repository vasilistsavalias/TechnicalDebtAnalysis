package com.gdx.game.quest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import static com.gdx.game.quest.QuestTask.QuestType.FETCH;

public class QuestGraph {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestGraph.class);

    private Hashtable<String, QuestTask> questTasks;
    private Hashtable<String, ArrayList<QuestTaskDependency>> questTaskDependencies;
    private String questTitle;
    private String questID;
    private boolean isQuestComplete;
    private int goldReward;
    private int xpReward;

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }

    public boolean isQuestComplete() {
        return isQuestComplete;
    }

    public void setQuestComplete(boolean isQuestComplete) {
        this.isQuestComplete = isQuestComplete;
    }

    public String getQuestID() {
        return questID;
    }

    public void setQuestID(String questID) {
        this.questID = questID;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public boolean areAllTasksComplete() {
        ArrayList<QuestTask> tasks = getAllQuestTasks();
        for(QuestTask task: tasks) {
            if(!task.isTaskComplete()) {
                return false;
            }
        }
        return true;
    }

    public void setTasks(Hashtable<String, QuestTask> questTasks) {
        if(questTasks.size() < 0) {
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this.questTasks = questTasks;
        this.questTaskDependencies = new Hashtable<>(questTasks.size());

        for(QuestTask questTask: questTasks.values()) {
            questTaskDependencies.put(questTask.getId(), new ArrayList<>());
        }
    }

    public ArrayList<QuestTask> getAllQuestTasks() {
        Enumeration<QuestTask> enumeration = questTasks.elements();
        return Collections.list(enumeration);
    }

    public void clear() {
        questTasks.clear();
        questTaskDependencies.clear();
    }

    public boolean isValid(String taskID) {
        QuestTask questTask = questTasks.get(taskID);
        return questTask != null;
    }

    public boolean isReachable(String sourceID, String sinkID) {
        if(!isValid(sourceID) || !isValid(sinkID)) {
            return false;
        }
        if(questTasks.get(sourceID) == null) {
            return false;
        }

        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(sourceID);
        if(list == null) {
            return false;
        }
        for(QuestTaskDependency dependency: list) {
            if(dependency.getSourceId().equalsIgnoreCase(sourceID) && dependency.getDestinationId().equalsIgnoreCase(sinkID)) {
                return true;
            }
        }
        return false;
    }

    public QuestTask getQuestTaskByID(String id) {
        if(!isValid(id)) {
            //System.out.println("Id " + id + " is not valid!");
            return null;
        }
        return questTasks.get(id);
    }

    public void addDependency(QuestTaskDependency questTaskDependency) {
        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(questTaskDependency.getSourceId());
        if(list == null) {
            return;
        }

        //Will not add if creates cycles
        if(doesCycleExist(questTaskDependency)) {
            //System.out.println("Cycle exists! Not adding");
            return;
        }

        list.add(questTaskDependency);
    }

    public boolean doesCycleExist(QuestTaskDependency questTaskDep) {
        Set<String> keys = questTasks.keySet();
        for(String id: keys) {
            if(doesQuestTaskHaveDependencies(id) && questTaskDep.getDestinationId().equalsIgnoreCase(id)) {
                    //System.out.println("ID: " + id + " destID: " + questTaskDep.getDestinationId());
                    return true;
                }
            }
        return false;
    }

    public boolean doesQuestTaskHaveDependencies(String id) {
        QuestTask task = getQuestTaskByID(id);
        if(task == null) {
            return false;
        }
        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(id);

        return !list.isEmpty();
    }

    public boolean updateQuestForReturn() {
        ArrayList<QuestTask> tasks = getAllQuestTasks();
        QuestTask readyTask = null;

        //First, see if all tasks are available, meaning no blocking dependencies
        for(QuestTask task : tasks) {
            if(!isQuestTaskAvailable(task.getId())) {
                return false;
            }
            if(!task.isTaskComplete()) {
                if(task.getQuestType().equals(QuestTask.QuestType.RETURN)) {
                    readyTask = task;
                } else {
                    return false;
                }
            }
        }
        if(readyTask == null) {
            return false;
        }
        readyTask.setTaskComplete();
        return true;
    }

    public boolean isQuestTaskAvailable(String id) {
        QuestTask task = getQuestTaskByID(id);
        if(task == null) {
            return false;
        }
        ArrayList<QuestTaskDependency> list = questTaskDependencies.get(id);

        for(QuestTaskDependency dep: list) {
            QuestTask depTask = getQuestTaskByID(dep.getDestinationId());
            if(depTask == null || depTask.isTaskComplete()) {
                continue;
            }
            if(dep.getSourceId().equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }

    public void setQuestTaskComplete(String id) {
        QuestTask task = getQuestTaskByID(id);
        if(task == null) {
            return;
        }
        task.setTaskComplete();
    }

    public void update(MapManager mapMgr) {
        Handler.RunRutime(mapMgr, this, Handler.Action.UPDATE);
    }

    public void init(MapManager mapMgr) {
        Handler.RunRutime(mapMgr, this, Handler.Action.INIT);
    }

    public String toString() {
        return questTitle;
    }

    public String toJson() {
        Json json = new Json();
        return json.prettyPrint(this);
    }

    public void printLOGGER(String message) {
        LOGGER.debug(message);
    }
}
