package net.pinodev.ultraplaytime.commands.player;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.commands.CommandHandler;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.commands.player.subcommands.CheckOthers;
import net.pinodev.ultraplaytime.commands.player.subcommands.NextReward;
import net.pinodev.ultraplaytime.commands.player.subcommands.TopPlayers;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class PlayerCommand extends CommandHandler implements CommandExecutor, TabExecutor {

    private final List<String> tabComplete;

    public PlayerCommand(){
        registerSubCommands();
        tabComplete = new ArrayList<>(subCommands.keySet());
    }
    @Override
    public void registerSubCommands() {
        SubCommand checkOthers = new CheckOthers("info" ,"ultraplaytime.checkothers", true);
        subCommands.put(checkOthers.getName(), checkOthers);
        SubCommand nextReward = new NextReward("reward", "ultraplaytime.nextreward", false);
        subCommands.put(nextReward.getName(), nextReward);
        SubCommand topPlayers =  new TopPlayers("top", "ultraplytime.top", false);
        subCommands.put(topPlayers.getName(), topPlayers);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            if(sender instanceof ConsoleCommandSender){
                sender.sendMessage(Locale.PREFIX.getStripped() + " Only players can see their playtime!");
                return true;
            }else{
                final Player player = (Player) sender;
                UtilsManager.message.sendPlaytime(Locale.PLAYER_PLAYTIME, player, sender);
            }

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
