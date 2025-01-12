package net.pinodev.ultraplaytime.tasks;

import net.pinodev.ultraplaytime.cache.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.database.queries.Statements.*;

public class TasksQuit {

    public CompletableFuture<Void> saveUser(final User user){
        long time = System.currentTimeMillis();
        return CompletableFuture.runAsync(()->{
            try{
                final UUID userUUID = user.getUuid();
                final Long playtime = user.getPlaytime();
                final byte[] rewards = (user.getRewardsAchieved().size() == 0) ? null
                        : UtilsManager.rewards.compressed(UtilsManager.rewards.toArray(user.getRewardsAchieved()));
                try(Connection conn = Database.getConnection();
                    PreparedStatement ps = user.isNewbie() ?
                            conn.prepareStatement(INSERT_USER_DATA.getStatement()) :
                            conn.prepareStatement(UPDATE_USER_DATA.getStatement())){
                    if(user.isNewbie()){
                        ps.setBytes(1, UtilsManager.uuid.toBytes(userUUID));
                        ps.setLong(2, playtime);
                        ps.setBytes(3, rewards);
                    }else{
                        ps.setLong(1, playtime);
                        ps.setBytes(2, rewards);
                        ps.setBytes(3, UtilsManager.uuid.toBytes(userUUID));
                    }
                    ps.executeUpdate();
                    UserManager.removeUser(userUUID);
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            logger.info("Took QUIT " + (System.currentTimeMillis() - time) + "ms");
        });
    }
}
