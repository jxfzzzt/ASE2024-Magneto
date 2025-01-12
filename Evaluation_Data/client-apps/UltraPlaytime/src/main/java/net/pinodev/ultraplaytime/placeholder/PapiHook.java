package net.pinodev.ultraplaytime.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pinodev.ultraplaytime.cache.TopUser;
import net.pinodev.ultraplaytime.cache.User;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.pinodev.ultraplaytime.UltraPlaytime.UserManager;
import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;
import static net.pinodev.ultraplaytime.configs.files.Rewards.REWARD_DISPLAYNAME;

public class PapiHook extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ultraplaytime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "pino";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params){
        if(params.equalsIgnoreCase("time")){
            return playerPlaytime(player);
        }else if(params.equalsIgnoreCase("current_reward")){
            return UtilsManager.rewards.getCurrentReward(player);
        }else if(params.startsWith("top_name_")){
            final String sPos = params.substring(params.lastIndexOf("_") + 1);
            Integer nPos = (UtilsManager.playtime.isInteger(sPos)) ? Integer.parseInt(sPos) : null;
            return (nPos != null) ? topUserName(nPos-1) : null;
        }else if(params.startsWith("top_time_")){
            final String sPos = params.substring(params.lastIndexOf("_") + 1);
            Integer nPos = (UtilsManager.playtime.isInteger(sPos)) ? Integer.parseInt(sPos) : null;
            return (nPos != null) ? topUserPlaytime(nPos-1) : null;
        }else if(params.equalsIgnoreCase("rank_display_name")){
            String currentReward = UtilsManager.rewards.getCurrentReward(player);
            return UtilsManager.message.colorized(REWARD_DISPLAYNAME.getString(Integer.parseInt(currentReward)));
        }
        return null;
    }

    private String playerPlaytime(OfflinePlayer player){
        final UUID uuid = player.getUniqueId();
        if(UserManager.getCachedUsers().containsKey(uuid)){
            final User user = UserManager.getCachedUsers().get(uuid);
            final long playtime = user.getPlaytime();
            return UtilsManager.playtime.formatted(playtime);
        }
        return "0";
    }


    private String topUserName(int position){
        if(position > 9 || position < 0) return null;
        final TopUser topUser = UserManager.getTopUserAt(position);
        if(topUser == null) return "MISSING";
        return topUser.getUserName();
    }

    private String topUserPlaytime(int position){
        if(position > 9 || position < 0) return null;
        final TopUser topUser = UserManager.getTopUserAt(position);
        if(topUser == null) return "0";
        final UUID uuid = topUser.getUuid();
        if(UserManager.getCachedUsers().containsKey(uuid)){
            final User user = UserManager.getCachedUsers().get(uuid);
            return UtilsManager.playtime.formatted(user.getPlaytime());
        }else{
            return UtilsManager.playtime.formatted(topUser.getPlaytime());
        }
    }
}
