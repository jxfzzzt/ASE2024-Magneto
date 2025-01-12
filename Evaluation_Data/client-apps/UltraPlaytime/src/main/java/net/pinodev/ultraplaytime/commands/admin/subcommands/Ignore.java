package net.pinodev.ultraplaytime.commands.admin.subcommands;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

import static net.pinodev.ultraplaytime.UltraPlaytime.TasksManager;
import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class Ignore extends SubCommand {
    public Ignore(String name, String permission, boolean consoleAllowed) {
        super(name, permission, consoleAllowed);
    }

    @Override
    public void execute(CommandSender executor, String[] args) {
        if(args.length != 2){
            UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
        }else {
            String targetName = args[1];
            final Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                UtilsManager.message.send(Locale.TARGET_NOT_FOUND.getString().replace("{player}", targetName), executor);
            }else{
                TasksManager.leaderboard.
                        handleIgnore(executor, targetName, target.getUniqueId())
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            UtilsManager.message.send(Locale.GENERAL_ERROR, executor, null);
                            return null;
                        });
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender executor, String[] args) {
            if(args.length > 2){
                return ImmutableList.of();
            }
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
