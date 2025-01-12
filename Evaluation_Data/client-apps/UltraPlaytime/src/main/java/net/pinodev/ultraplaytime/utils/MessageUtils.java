package net.pinodev.ultraplaytime.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.pinodev.ultraplaytime.cache.User;
import net.pinodev.ultraplaytime.placeholder.Placeholder;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.pinodev.ultraplaytime.UltraPlaytime.UserManager;
import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;
import static net.pinodev.ultraplaytime.configs.files.Rewards.REWARD_DISPLAYNAME;

public class MessageUtils {
    public String colorized(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public void sendPlaytime(Locale locale, Player target, CommandSender sender){
        final User user = UserManager.getCachedUsers().get(target.getUniqueId());
        final Long playtime = user.getPlaytime();
        final String currentReward = UtilsManager.rewards.getCurrentReward(target);
        List<Placeholder> placeholders = new ArrayList<>();
        placeholders.add(new Placeholder("{playtime}", UtilsManager.playtime.formatted(playtime)));
        placeholders.add(new Placeholder("{rewards}", currentReward));
        placeholders.add(new Placeholder("{rank_name}", REWARD_DISPLAYNAME.getString(Integer.parseInt(currentReward))));
        if(locale == Locale.TARGET_PLAYTIME){
            placeholders.add(new Placeholder("{player}", target.getName()));
        }
        send(locale, sender, placeholders);
    }

    public void send(Locale locale, CommandSender target, List<Placeholder> placeholders){
        if(locale.type == String.class){
            if (placeholders == null) {
                send(locale.getString(), target);
            } else {
                send(locale.getString(), target, placeholders);
            }
        } else if (locale.type == List.class) {
            if (placeholders == null) {
                send(locale.getStringList(), target);
            } else {
                send(locale.getStringList(), target, placeholders);
            }
        }
    }


    public void send(String message, CommandSender target, List<Placeholder> placeholders){
        message = colorized(message.replace("{prefix}", Locale.PREFIX.getString()));
        target.sendMessage(replacePlaceholders(message, placeholders));
    }

    public void send(String message, CommandSender target){
        message = colorized(message.replace("{prefix}", Locale.PREFIX.getString()));
        target.sendMessage(message);
    }

    public void send(List<String> messages, CommandSender target, List<Placeholder> placeholders){
        for(String message : messages){
            message = colorized(message.replace("{prefix}", Locale.PREFIX.getString()));
            target.sendMessage(replacePlaceholders(message, placeholders));
        }
    }

    public void send(List<String> messages, CommandSender target){
        for(String message : messages){
            message = colorized(message.replace("{prefix}", Locale.PREFIX.getString()));
            target.sendMessage(message);
        }
    }

    public void sendTopPlayers(List<String> messages, Player player, BaseComponent nextPage, BaseComponent previousPage){
        for(String message : messages){
            message = colorized(message);
            if(message.contains("{next-page}") && message.contains("{prev-page}")){
                previousPage.addExtra(nextPage);
//                player.spigot().sendMessage(previousPage);
            }else if (message.contains("{prev-page}")) {
//                player.spigot().sendMessage(previousPage);
            } else if (message.contains("{next-page}")) {
//                player.spigot().sendMessage(nextPage);
            }else{
                message = PlaceholderAPI.setPlaceholders(player, message);
                player.sendMessage(message);
            }
        }
    }


    public void sendTitle(String upperTitle, String subTitle, final Player target){
        upperTitle = colorized(upperTitle);
        subTitle = colorized(subTitle);
//        target.sendTitle(
//                upperTitle,
//                subTitle
//        );
    }

    public String replacePlaceholders(String text, List<Placeholder> placeholders){
        for(Placeholder placeholder : placeholders){
            text = text.replace(placeholder.getToSubstitute(), placeholder.getSubstitutor());
        }
        return colorized(text);
    }
}
