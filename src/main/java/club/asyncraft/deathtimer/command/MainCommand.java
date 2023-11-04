package club.asyncraft.deathtimer.command;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.DeadManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            return false;
        }
        if (strings.length == 1) {
            if ("reload".equalsIgnoreCase(strings[0])) {
                if (!commandSender.hasPermission("deathtimer.reload")) {
                    commandSender.sendMessage(TranslatableText.create("command.without_permission"));
                    return true;
                }
                DeathTimer.instance.initConfig();
                commandSender.sendMessage(TranslatableText.create("command.reload.reloaded"));
                return true;
            }

            if ("revive".equalsIgnoreCase(strings[0])) {
//                if (!commandSender.hasPermission("deathtimer.respawn")) {
//                    commandSender.sendMessage("你没有权限");
//                    return true;
//                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(TranslatableText.create("command.non_console"));
                } else {
                    Player player = (Player) commandSender;
                    if (DeadManager.check(player.getUniqueId().toString())) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(TranslatableText.create("command.revive.revived"));
                    } else {
                        player.sendMessage(TranslatableText.create("command.revive.time_left"));
                    }
                }
                return true;
            }
        }

        return false;
    }
}
