package net.pinodev.ultraplaytime.commands.admin.subcommands;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.cache.User;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.configs.files.Locale;
import net.pinodev.ultraplaytime.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.pinodev.ultraplaytime.UltraPlaytime.UserManager;
import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class SetTime extends SubCommand {
    public SetTime(String name, String permission, boolean consoleAllowed) {
        super(name, permission, consoleAllowed);
    }

    @Override
    public void execute(CommandSender executor, String[] args) {
        if(args.length != 3){
            UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
        }else {
            String targetName = args[1];
            final Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                UtilsManager.message.send(Locale.TARGET_NOT_FOUND.getString().replace("{player}", targetName), executor);
            }else{
                String timeToSet = args[2];
                if(UtilsManager.playtime.isInteger(timeToSet)){
                    long playtime = Long.parseLong(timeToSet)*1000;
                    setTargetTime(executor, target, playtime);
                }else{
                    UtilsManager.message.send(Locale.NOT_NUMBER, executor, null);
                }
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

    private void setTargetTime(CommandSender executor, Player target, Long playtime){
        if(playtime < 0){
            UtilsManager.message.send(Locale.NOT_NUMBER, executor, null);
        }else{
            final UUID uuid = target.getUniqueId();
            User user = UserManager.getCachedUsers().get(uuid);
            user.setPlaytime(playtime);
            List<Placeholder> placeholders = new ArrayList<>();
            placeholders.add(new Placeholder("{player}", target.getName()));
            UtilsManager.message.send(Locale.SET_TIME, executor, placeholders);
        }

    }
}
