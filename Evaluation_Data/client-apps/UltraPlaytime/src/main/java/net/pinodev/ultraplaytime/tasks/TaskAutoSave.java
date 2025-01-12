package net.pinodev.ultraplaytime.tasks;

import net.pinodev.ultraplaytime.cache.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.database.queries.Statements.SAVE_USER_DATA;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RED;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public class TaskAutoSave {

    public boolean isRunning = false;

    void autoSaveData(){
        logger.info("Starting autosaving...found " + UserManager.getCachedUsers().size() + " entries!");
                isRunning = true;
                try(Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement(SAVE_USER_DATA.getStatement())){
                    for(User user : UserManager.getCachedUsers().values()){
                        final byte[] uuid = UtilsManager.uuid.toBytes(user.getUuid());
                        final Long playtime = user.getPlaytime();
                        final byte[] rewards = (user.getRewardsAchieved().size() == 0) ? null
                                : UtilsManager.rewards.compressed(UtilsManager.rewards.toArray(user.getRewardsAchieved()));
                        ps.setBytes(1, uuid);
                        ps.setLong(2, playtime);
                        ps.setBytes(3, rewards);
                        ps.addBatch();
                        if(user.isOffline()){
                            UserManager.removeUser(user.getUuid());
                        }
                    }
                    final int[] result = ps.executeBatch();
                    for(int i : result){
                        if(i == PreparedStatement.EXECUTE_FAILED){
                            logger.info(ANSI_RED.getColor() + "ERROR: "+ ANSI_RESET.getColor() + "Failed " + ps);
                        }
                    }
                    logger.info("Saved "+result.length+" users into the database!");
                }catch (SQLException  | IOException exception){
                    logger.info(ANSI_RED.getColor() + "ERROR: "+ ANSI_RESET.getColor() + "Failed to save data to database!");
                    exception.printStackTrace();
                }finally{
                    isRunning = false;
                }
    }
}
