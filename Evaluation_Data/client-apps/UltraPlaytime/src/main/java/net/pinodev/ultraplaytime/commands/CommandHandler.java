package net.pinodev.ultraplaytime.commands;

import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public abstract class CommandHandler {

    protected final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandHandler() {
        registerSubCommands();
    }

    public abstract void registerSubCommands();

    public boolean handleCommand(CommandSender commandSender, String[] strings) {

        if (subCommands.containsKey(strings[0])) {

            SubCommand subCommand = subCommands.get(strings[0]);

            if (!commandSender.hasPermission(subCommand.getPermission())) {
                UtilsManager.message.send(Locale.NO_PERMISSION, commandSender, null);
                return true;
            }

            if (!subCommand.isConsoleAllowed() && !(commandSender instanceof org.bukkit.entity.Player)) {
                commandSender.sendMessage(Locale.PREFIX.getStripped() + " Only players can run this command!");
                return true;
            }

            subCommand.execute(commandSender, strings);

            return true;
        }

        UtilsManager.message.send(Locale.INVALID_COMMAND, commandSender, null);


        return true;
    }

}
