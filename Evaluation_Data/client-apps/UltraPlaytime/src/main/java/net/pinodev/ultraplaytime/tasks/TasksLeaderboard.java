package net.pinodev.ultraplaytime.tasks;

import net.pinodev.ultraplaytime.cache.TopUser;
import net.pinodev.ultraplaytime.configs.files.Locale;
import net.pinodev.ultraplaytime.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.database.queries.Statements.*;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RED;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public class TasksLeaderboard {

    void getLeaderboard(){
            UserManager.getTopUsers().clear();
            try(Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_TOP_USERS.getStatement());
                ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    final UUID uuid = UtilsManager.uuid.fromBytes(rs.getBytes("player_uuid"));
                    String username = Bukkit.getOfflinePlayer(uuid).getName();
                    if(username == null) username = "UNKNOWN";
                    final Long playtime = rs.getLong("playtime");
                    UserManager.addTopUser(new TopUser(
                            uuid,
                            playtime,
                            username
                    ));
                }
                logger.info("Fetched "+ UserManager.getTopUsers().size() + "/10 Top Users from database!");
            }catch (SQLException exception){
                logger.info(ANSI_RED.getColor() + "ERROR: "+ANSI_RESET.getColor() + " while fetching leaderboard... retrying in a while!");
                exception.printStackTrace();
            }
    }

    public CompletableFuture<Void> handleIgnore(CommandSender sender , String targetName, UUID targetUUID){
        return CompletableFuture.runAsync(() -> {
            checkIfIgnored(targetUUID).thenAccept(ignored -> {
                List<Placeholder> placeholders = new ArrayList<>();
                placeholders.add(new Placeholder("{player}", targetName));
                if (ignored) {
                    removeIgnored(sender, targetUUID, placeholders);
                } else {
                    addIgnored(sender, targetUUID, placeholders);
                }
            });
        });
    }

    private CompletableFuture<Boolean> checkIfIgnored(UUID uuid){
        return CompletableFuture.supplyAsync(() ->{
            try(Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_NO_LEADERBOARD_USER.getStatement())){
                ps.setBytes(1, UtilsManager.uuid.toBytes(uuid));
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }catch (SQLException exception){
                logger.info(ANSI_RED.getColor() + "ERROR: "+ANSI_RESET.getColor() + " while checking if a player is ignored from leaderboard!");
                exception.printStackTrace();
            }
            return false;
        });
    }
    private void addIgnored(CommandSender sender, UUID uuid, List<Placeholder> placeholders){
        CompletableFuture.runAsync(() -> {
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(ADD_NO_LEADERBOARD_USER.getStatement())) {
                ps.setBytes(1, UtilsManager.uuid.toBytes(uuid));
                ps.executeUpdate();
                UtilsManager.message.send(Locale.ADDED_LEADERBOARD_IGNORED, sender, placeholders);
                logger.info("Added " + uuid.toString() + " to leaderboard ignored players");
            } catch (SQLException exception) {
                UtilsManager.message.send(Locale.GENERAL_ERROR, sender, placeholders);
                logger.info(ANSI_RED.getColor() + "ERROR: " + ANSI_RESET.getColor() + " while adding a player to ignored players from leaderboard!");
                exception.printStackTrace();
            }
        });
    }

    private void removeIgnored(CommandSender sender, UUID uuid, List<Placeholder> placeholders){
        CompletableFuture.runAsync(() ->{
            try(Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(REMOVE_NO_LEADERBOARD_USER.getStatement())){
                ps.setBytes(1, UtilsManager.uuid.toBytes(uuid));
                ps.executeUpdate();
                UtilsManager.message.send(Locale.REMOVED_LEADERBOARD_IGNORED, sender, placeholders);
                logger.info("Removed " + uuid.toString() + " from leaderboard ignored players");
            }catch (SQLException exception){
                UtilsManager.message.send(Locale.GENERAL_ERROR, sender, placeholders);
                logger.info(ANSI_RED.getColor() + "ERROR: " + ANSI_RESET.getColor() + " while removing a player to ignored players from leaderboard!");
                exception.printStackTrace();
            }
        });
    }

}
