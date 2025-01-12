package net.pinodev.ultraplaytime.listeners;

import net.pinodev.ultraplaytime.cache.AfkUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RED;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            final UUID uuid = event.getUniqueId();
            final String username = event.getName();
            if (!UserManager.getCachedUsers().containsKey(uuid)) {
                TasksManager.join.loadUser(uuid)
                .exceptionally(throwable -> {
                    logger.info(ANSI_RED.getColor() + "ERROR: "+ ANSI_RESET.getColor()+" Impossible to get data for "+username);
                    throwable.printStackTrace();
                    return null;
                });
            }
        }
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
            TasksManager.afkTask.usersAfk.put(uuid, new AfkUser(player.getLocation(), 0));

    }
}
