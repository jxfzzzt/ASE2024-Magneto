package net.pinodev.ultraplaytime.tasks;

import net.pinodev.ultraplaytime.cache.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.configs.files.Settings.MULTI_SERVER;
import static net.pinodev.ultraplaytime.configs.files.Settings.MULTI_SERVER_LATENCY;
import static net.pinodev.ultraplaytime.database.queries.Statements.GET_USER_DATA;

public class TasksJoin {

    public CompletableFuture<Void> loadUser(UUID uuid){
        return CompletableFuture.runAsync(()->{
            long time = System.currentTimeMillis();
            if(MULTI_SERVER.getBoolean()){
                waitForLatency()
                        .thenCompose(getData -> getUserData(uuid))
                        .thenRun(()-> logger.info("Took JOIN-A " + (System.currentTimeMillis() - time) + "ms"))
                        .join();
            }else{
                getUserData(uuid)
                        .thenRun(()-> logger.info("Took JOIN-B " + (System.currentTimeMillis() - time) + "ms"))
                        .join();
            }
        });
    }

    private CompletableFuture<Void> waitForLatency(){
        return CompletableFuture.runAsync(()->{
            try{
                Thread.sleep(MULTI_SERVER_LATENCY.getLong());
            }catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }

    /*
    Async checking player existence in the database
     */

    private CompletableFuture<Void> getUserData(UUID uuid){
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareCall(GET_USER_DATA.getStatement())
            ) {
                ps.setBytes(1, UtilsManager.uuid.toBytes(uuid));
                try (ResultSet rs = ps.executeQuery()) {
                    final long playtime;
                    final int[] rewardsArray;
                    final boolean isNewbie;
                    if (rs.next()) {
                        playtime = rs.getLong("playtime");
                        rewardsArray = (rs.getBytes("rewards") != null)
                                ? UtilsManager.rewards.decompressed(rs.getBytes("rewards"))
                                : new int[]{};
                        isNewbie = false;
                    } else {
                        playtime = 0;
                        rewardsArray = new int[]{};
                        isNewbie = true;
                    }
                    UserManager.addUser(uuid, new User(
                            uuid,
                            playtime,
                            UtilsManager.rewards.toHashSet(rewardsArray),
                            isNewbie,
                            false,
                            false
                    ));
                }
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
