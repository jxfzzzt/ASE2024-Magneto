package net.pinodev.ultraplaytime.migration;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.database.queries.Statements.SAVE_USER_DATA;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RED;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public class CMIProvider extends Provider{


    public CMIProvider(String pluginName) {
        super(pluginName);
    }


    @Override
    public void executeMigration(CommandSender executor) {
        UtilsManager.message.send(Locale.MIGRATION_STARTED, executor, null);
        Map<UUID, Long> pendingUsers = new HashMap<>();
//        for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
//            CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
//            pendingUsers.put(user.getUniqueId(), user.getTotalPlayTime());
//        }
        try(Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_USER_DATA.getStatement())){
            int batchSize = 0;
            for(UUID uuid : pendingUsers.keySet()){
                final Long playtime = pendingUsers.get(uuid);
                int[] rewards = UtilsManager.rewards.calculateRewards(playtime);
                ps.setBytes(1, UtilsManager.uuid.toBytes(uuid));
                ps.setLong(2, playtime);
                ps.setBytes(3, UtilsManager.rewards.compressed(rewards));
                ps.addBatch();
                batchSize++;
            }
            final int[] result = ps.executeBatch();
            for(int i : result){
                if(i == PreparedStatement.EXECUTE_FAILED){
                    logger.info(ANSI_RED.getColor() + "ERROR: "+ ANSI_RESET.getColor() + "Failed " + ps);
                }
            }
            sendTitle(executor, "&aFound...", batchSize);
            UserManager.getCachedUsers().clear();
            UtilsManager.message.send(Locale.MIGRATION_COMPLETED, executor, null);

        }catch (SQLException | IOException exception){
            UtilsManager.message.send(Locale.MIGRATION_FAILED, executor, null);
            exception.printStackTrace();
        }
    }


    private void sendTitle(CommandSender executor, String upperTitle, int userFetched){
        if(executor instanceof Player){
            Player player = (Player) executor;
            UtilsManager.message.sendTitle(upperTitle, "&e"+userFetched+" users", player);
        }
    }

}
