package net.pinodev.ultraplaytime;

import net.pinodev.ultraplaytime.cache.UserManager;
import net.pinodev.ultraplaytime.commands.admin.AdminCommand;
import net.pinodev.ultraplaytime.commands.player.PlayerCommand;
import net.pinodev.ultraplaytime.configs.ConfigFiles;
import net.pinodev.ultraplaytime.configs.files.Settings;
import net.pinodev.ultraplaytime.database.DatabaseManager;
import net.pinodev.ultraplaytime.database.connectors.H2;
import net.pinodev.ultraplaytime.database.connectors.MariaDB;
import net.pinodev.ultraplaytime.database.connectors.MySQL;
import net.pinodev.ultraplaytime.libs.LibManager;
import net.pinodev.ultraplaytime.listeners.PlayerJoin;
import net.pinodev.ultraplaytime.listeners.PlayerQuit;
import net.pinodev.ultraplaytime.placeholder.PapiHook;
import net.pinodev.ultraplaytime.rewards.RewardManager;
import net.pinodev.ultraplaytime.tasks.TasksManager;
import net.pinodev.ultraplaytime.utils.UtilsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static net.pinodev.ultraplaytime.configs.files.Settings.DB_TYPE;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_GREEN;
import static net.pinodev.ultraplaytime.utils.LogUtils.ANSI_RESET;

public final class UltraPlaytime extends JavaPlugin {

    public static UltraPlaytime MainInstance;

    public static final Logger logger = Logger.getLogger("UltraPlayTime");

    private final PluginManager pluginManager = getServer().getPluginManager();


    /*
    File configuration instances
     */
    public static ConfigFiles SettingsYML;

    public static ConfigFiles LangYML;

    public static ConfigFiles RewardsYML;

    /*
    Database
     */

    public static DatabaseManager Database;

    /*
    Tasks
     */

    public static TasksManager TasksManager;

    /*
    Cache
     */
    public static UserManager UserManager;

    /*
    Reward Manager
     */

    public static RewardManager RewardManager;

    /*
    Utils
     */

    public static UtilsManager UtilsManager;

    static {
        UtilsManager = new UtilsManager();
        UserManager = new UserManager();
        TasksManager = new TasksManager();
        RewardManager = new RewardManager();
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        logger.info(ANSI_GREEN.getColor() + "Plugin is loading...." + ANSI_RESET.getColor());
        setInstances();
        setListeners();
        setCommands();
        registerSoftDependencies();
        logger.info("Took " + (System.currentTimeMillis() - startTime) + " ms");

    }

    @Override
    public void onDisable() {
        TasksManager.shutdownScheduler();
        Database.closeHikariPool();
    }

    private void setInstances() {
        MainInstance = this;
        setConfigs();
        new LibManager();
        UtilsManager = new UtilsManager();
        UserManager = new UserManager();
        loadDatabase();
        if (!Settings.MULTI_SERVER_REWARDS.getBoolean()) {
            RewardManager = new RewardManager();
        }
        TasksManager = new TasksManager();

    }

    private void setConfigs() {
        SettingsYML = new ConfigFiles("settings.yml", getDataFolder());
        LangYML = new ConfigFiles("lang.yml", getDataFolder());
        RewardsYML = new ConfigFiles("rewards.yml", getDataFolder());
    }

    private void setListeners() {
        PlayerJoin playerJoin = new PlayerJoin();
        pluginManager.registerEvents(playerJoin, this);
        PlayerQuit playerQuit = new PlayerQuit();
        pluginManager.registerEvents(playerQuit, this);
    }

    private void registerSoftDependencies() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiHook().register();
            logger.info("[!] " + "Founded PlaceholdersAPI plugin");
            logger.info("[!] " + "all the related futures are activated");
        } else {
            logger.info("[!] " + "Impossible to found PlaceholdersAPI plugin");
            logger.info("[!] " + "all the related futures are disabled");
        }
    }

    private void setCommands() {
        PlayerCommand playtimeCommand = new PlayerCommand();
        loadCommand("playtime", playtimeCommand, playtimeCommand);
        AdminCommand adminCommand = new AdminCommand();
        loadCommand("playtimeadmin", adminCommand, adminCommand);
    }


    private void loadDatabase() {
        switch (DB_TYPE.getString().toLowerCase()) {
            case "h2":
                Database = new H2();
                break;
            case "mysql":
                Database = new MySQL();
                break;
            case "mariadb":
                Database = new MariaDB();
                break;
            default:
                logger.info("!!! Invalid database type specified -> " + DB_TYPE.getString());
                logger.info("!!! Switching to default H2");
                Database = new H2();
                break;

        }
    }

    private void loadCommand(String cmdName, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand cmd = getServer().getPluginCommand(cmdName);
        if (cmd != null) {
            cmd.setExecutor(executor);
            cmd.setTabCompleter(tabCompleter);
        } else {
            logger.info("Command " + cmdName + " not found!");
        }
    }
}
