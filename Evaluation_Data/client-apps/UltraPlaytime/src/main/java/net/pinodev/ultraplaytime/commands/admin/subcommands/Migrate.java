package net.pinodev.ultraplaytime.commands.admin.subcommands;

import com.google.common.collect.ImmutableList;
import net.pinodev.ultraplaytime.commands.SubCommand;
import net.pinodev.ultraplaytime.configs.files.Locale;
import net.pinodev.ultraplaytime.migration.CMIProvider;
import net.pinodev.ultraplaytime.migration.Provider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.pinodev.ultraplaytime.UltraPlaytime.UtilsManager;

public class Migrate extends SubCommand {

    private Map<String, Provider> providers;

    private boolean migrating = false;

    public Migrate(String name, String permission, boolean consoleAllowed) {
        super(name, permission, consoleAllowed);
        registerProviders();
    }


    @Override
    public void execute(CommandSender executor, String[] args) {
        if(args.length != 2){
            UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
        }else {
            String providerName = args[1];
            if(!providers.containsKey(providerName)){
                UtilsManager.message.send(Locale.INVALID_COMMAND, executor, null);
            }else{
                if(migrating){
                    UtilsManager.message.send(Locale.MIGRATION_RUNNING, executor, null);
                }else{
                    Provider provider = providers.get(providerName);
                    migrating = true;
                    provider.start(executor).thenRun(() -> migrating = false).exceptionally(
                            throwable -> {
                                throwable.printStackTrace();
                                UtilsManager.message.send(Locale.GENERAL_ERROR, executor, null);
                                migrating = false;
                                return null;
                            }
                    );
                }

            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender executor, String[] args) {
        if(args.length > 2){
            return ImmutableList.of();
        }
        return new ArrayList<>(providers.keySet());
    }

    private void registerProviders(){
        providers = new HashMap<>();
        Provider cmiProvider = new CMIProvider("CMI");
        providers.put("CMI", cmiProvider);
    }


}
