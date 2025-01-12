package net.pinodev.ultraplaytime.configs;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface ManagerFiles {


    /*
    Access to the child file
     */
    File getConfigFile();

    /*
    Access to the configuration file
     */
    FileConfiguration getFileConfiguration();

    /*
    Check for existence of the child file and save it
     */
    void save(String child, File dataFolder);

    /*
    Reload the configuration file
     */
    void reload(CommandSender sender);


}
