package net.pinodev.ultraplaytime.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public abstract class SubCommand {

    @Getter
    private final String name;
    @Getter private final String permission;
    @Getter private final boolean consoleAllowed;

    public abstract void execute(CommandSender executor, String[] args);
    public abstract List<String> tabComplete(CommandSender executor, String[] args);
}

