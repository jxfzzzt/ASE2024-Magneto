package net.pinodev.ultraplaytime.commands.player.subcommands;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.configs.files.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class TopPlayers extends SubCommand {
    public TopPlayers(String name, String permission, boolean consoleAllowed) {
        super(name, permission, consoleAllowed);
    }

    @Override
    public void execute(CommandSender executor, String[] args) {
        if(args.length != 2){
            UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
        }else{
            final Player player = (Player) executor;
            String page = args[1];
            if(UtilsManager.playtime.isInteger(page)){
                sendTopList(player, Integer.parseInt(page));
            }else{
                UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender executor, String[] args) {
        if(args.length == 2){
            final ConfigurationSection config = Locale.TOP_PLAYERS.getConfigurationSection();
            List<String> pages = new ArrayList<>(config.getKeys(false));
            pages.remove("next-page");
            pages.remove("prev-page");
            return pages;
        }
        return ImmutableList.of();
    }

    private void sendTopList(Player player, int page){
        final ConfigurationSection config = Locale.TOP_PLAYERS.getConfigurationSection();
        BaseComponent nextPage = new TextComponent(UtilsManager.message.colorized(config.getString("next-page")));
        nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playtime top "+ (page+1)));
        BaseComponent previousPage = new TextComponent(UtilsManager.message.colorized(config.getString("prev-page")));
        previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playtime top " + (page-1)));
        UtilsManager.message.sendTopPlayers(config.getStringList(String.valueOf(page)), player, nextPage, previousPage);
    }
}
