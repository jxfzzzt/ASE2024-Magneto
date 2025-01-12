package net.pinodev.ultraplaytime.commands.admin;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.commands.CommandHandler;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.commands.admin.subcommands.*;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class AdminCommand extends CommandHandler implements CommandExecutor, TabExecutor {

    private final List<String> tabComplete;

    public AdminCommand(){
        registerSubCommands();
        tabComplete = new ArrayList<>(subCommands.keySet());
    }
    @Override
    public void registerSubCommands() {
        SubCommand setTime = new SetTime("settime", "ultraplaytime.settime", true);
        subCommands.put(setTime.getName(), setTime);
        SubCommand setReward = new SetReward("setReward", "ultraplaytime.setReward", true);
        subCommands.put(setReward.getName(), setReward);
        SubCommand reload = new Reload("reload", "ultraplaytime.reload", true);
        subCommands.put(reload.getName(), reload);
        SubCommand help = new Help("help", "ultraplaytime.help", true);
        subCommands.put(help.getName(), help);
        SubCommand migrate = new Migrate("migrate", "ultraplaytime.migrate", true);
        subCommands.put(migrate.getName(), migrate);
        SubCommand ignore = new Ignore("ignore", "ultraplaytime.ignore", true);
        subCommands.put(ignore.getName(), ignore);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            UtilsManager.message.send(Locale.INVALID_COMMAND, sender,null);
            return true;
        }
        return handleCommand(sender, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1){
            List<String> filteredTabComplete = new ArrayList<>();

            for (String subCommandName : tabComplete) {
                SubCommand subCommand = subCommands.get(subCommandName);
                if (subCommand != null && sender.hasPermission(subCommand.getPermission())) {
                    filteredTabComplete.add(subCommandName);
                }
            }
            return filteredTabComplete;
        }
        if(args.length >= 2){
            if (subCommands.containsKey(args[0])) {
                return subCommands.get(args[0]).tabComplete(sender, args);
            }
        }
        return ImmutableList.of();
    }
}
