package net.pinodev.ultraplaytime.configs;

import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class ConfigFiles implements ManagerFiles {


    private File configFile;

    private FileConfiguration fileConfiguration;

    public ConfigFiles(String child, File dataFolder) {

        save(child, dataFolder);
    }

    @Override
    public File getConfigFile() {
        return configFile;
    }

    @Override
    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }


    @Override
    public void save(String child, File dataFolder) {
        configFile = new File(dataFolder, child);
        if (!configFile.exists()) {
            MainInstance.saveResource(child, false);
            logger.info("File " + child + " has been created");
        }
        fileConfiguration = loadConfiguration(configFile);
        logger.info("Correctly loaded " + configFile.getName());
    }

    @Override
    public void reload(CommandSender sender) {
        fileConfiguration = loadConfiguration(configFile);
        UtilsManager.message.send(Locale.RELOADED_FILE.getString().replace("{file}", configFile.getName()) , sender);
    }
}
