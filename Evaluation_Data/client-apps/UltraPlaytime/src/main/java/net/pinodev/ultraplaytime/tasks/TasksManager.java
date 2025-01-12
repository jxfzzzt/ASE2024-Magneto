package net.pinodev.ultraplaytime.tasks;

import net.pinodev.ultraplaytime.configs.files.Settings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.pinodev.ultraplaytime.configs.files.Settings.AUTO_SAVE;
import static net.pinodev.ultraplaytime.configs.files.Settings.LEADERBOARD_REFRESH;

public class TasksManager {

    private final ScheduledExecutorService scheduler;
    public final TasksLeaderboard leaderboard;

    private final TaskPlaytime playtimeUpdater;

    private TaskRewards rewards;

    public final TaskAfk afkTask;

    public final TaskAutoSave autoSave;
    public final TasksJoin join;

    public final TasksQuit quit;


    public TasksManager(){
        scheduler = Executors.newScheduledThreadPool(2);
        this.leaderboard = new TasksLeaderboard();
        this.playtimeUpdater = new TaskPlaytime();
        if(!Settings.MULTI_SERVER_REWARDS.getBoolean()){
            this.rewards = new TaskRewards();
        }
        this.afkTask = new TaskAfk();
        this.autoSave = new TaskAutoSave();
        this.join = new TasksJoin();
        this.quit = new TasksQuit();
        runRepeatingTasks();
    }

    private void runRepeatingTasks(){
        scheduler.scheduleAtFixedRate(this.playtimeUpdater::update, 0, 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this.leaderboard::getLeaderboard, 0, LEADERBOARD_REFRESH.getInt(), TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(this.afkTask::check, 0, 2, TimeUnit.SECONDS);
        if(!Settings.MULTI_SERVER_REWARDS.getBoolean()) {
            scheduler.scheduleAtFixedRate(this.rewards::check, 0, 2, TimeUnit.SECONDS);
        }
        if(!Settings.MULTI_SERVER.getBoolean()){
            scheduler.scheduleAtFixedRate(this.autoSave::autoSaveData, AUTO_SAVE.getInt() ,AUTO_SAVE.getInt(), TimeUnit.MINUTES);
        }
    }

    /*
    Called on server shutdown
    Trying to ensure saving all the data
     */
    public void shutdownScheduler(){
        if(autoSave.isRunning){
            scheduler.shutdown();
        }else{
            scheduler.shutdown();
            autoSave.autoSaveData();
        }
    }
}
