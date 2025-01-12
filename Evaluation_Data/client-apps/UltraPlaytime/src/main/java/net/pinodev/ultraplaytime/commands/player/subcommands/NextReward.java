package net.pinodev.ultraplaytime.commands.player.subcommands;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.cache.User;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.configs.files.Locale;
import net.pinodev.ultraplaytime.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;
import static net.pinodev.ultraplaytime.configs.files.Rewards.REWARD_DISPLAYNAME;

public class NextReward extends SubCommand {
    public NextReward(String name, String permission, boolean consoleAllowed) {
        super(name, permission, consoleAllowed);
    }

    @Override
    public void execute(CommandSender executor, String[] args) {
        if(args.length > 2){
            UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
        }else{
            final Player player = (Player) executor;
            int currentReward = Integer.parseInt(UtilsManager.rewards.getCurrentReward(player));
            final User user = UserManager.getCachedUsers().get(player.getUniqueId());
            final Long playtime = user.getPlaytime();
            checkNext(player, playtime, currentReward);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender executor, String[] args) {
        return ImmutableList.of();
    }

    private void checkNext(Player player, Long playtime, int currentReward){
        int nextReward = currentReward + 1;
        ConfigurationSection rewardConfig = RewardsYML.getFileConfiguration().getConfigurationSection("Rewards."+ nextReward);
        if(rewardConfig != null){
            long rewardRequirement = rewardConfig.getLong(".requirement");
            final long timeLeft = (rewardRequirement*1000) - playtime;
            List<Placeholder> placeholders = new ArrayList<>();
            placeholders.add(new Placeholder("{reward}", String.valueOf(nextReward)));
            placeholders.add(new Placeholder("{time}", UtilsManager.playtime.formatted(timeLeft)));
            placeholders.add(new Placeholder("{rank_name}", REWARD_DISPLAYNAME.getString(nextReward)));
            UtilsManager.message.send(Locale.TIME_NEXT_REWARD, player, placeholders);
        }else{
            UtilsManager.message.send(Locale.NO_MORE_REWARDS, player, null);
        }
    }
}
