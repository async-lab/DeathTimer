package com.asynclab.github.xiaomo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            return false;
        }
        if ("reload".equalsIgnoreCase(strings[0])) {
            deathtimer.instance.initConfig();
           commandSender.sendMessage("reloaded");
            return true;
        }
        return false;
    }
}
