package net.pinodev.ultraplaytime.migration;


import lombok.Getter;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public abstract class Provider {



    public Provider(String pluginName) {
        dependency = Bukkit.getPluginManager().getPlugin(pluginName);
    }
    private final Plugin dependency;
    public CompletableFuture<Void> start(CommandSender executor){
        if(dependency == null || !dependency.isEnabled()){
            UtilsManager.message.send(Locale.MIGRATION_NOT_FOUND, executor, null);
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.runAsync(() -> executeMigration(executor));
    }
    public abstract void executeMigration(CommandSender executor);
}
