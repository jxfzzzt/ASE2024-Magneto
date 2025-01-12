package net.pinodev.ultraplaytime.listeners;

import net.pinodev.ultraplaytime.cache.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.configs.files.Settings.MULTI_SERVER;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RED;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        final UUID uuid = event.getPlayer().getUniqueId();
        final String username = event.getPlayer().getName();
        final User user = UserManager.getCachedUsers().get(uuid);
        TasksManager.afkTask.usersAfk.remove(uuid);
        if(user != null){
            if(MULTI_SERVER.getBoolean()){
                TasksManager.quit.saveUser(user).exceptionally(throwable -> {
                    logger.info(ANSI_RED.getColor() + "ERROR: " + ANSI_RESET.getColor() + " Impossible to save data for " + username);
                    throwable.printStackTrace();
                    return null;
                });
            }else{
                user.setOffline(true);
            }
        }

    }
}
