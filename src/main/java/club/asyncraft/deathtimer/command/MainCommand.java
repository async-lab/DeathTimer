package club.asyncraft.deathtimer.command;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.DeadManager;
import club.asyncraft.deathtimer.util.Reference;
import org.bukkit.Bukkit;
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
                if (!commandSender.hasPermission(Reference.plugin_group + ".reload")) {
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
                    if (player.getGameMode() == GameMode.SPECTATOR && DeadManager.getRemainingTime(player.getUniqueId().toString()) == 0) {
                        player.setGameMode(GameMode.SURVIVAL);
                        DeadManager.revive(player);
                        player.sendMessage(TranslatableText.create("command.revive.revived"));
                    } else {
                        player.sendMessage(TranslatableText.create("command.revive.time_left"));
                    }
                }
                return true;
            }
        }

        if (strings.length == 2) {
            if ("clear".equalsIgnoreCase(strings[0])) {
                if (!commandSender.hasPermission(Reference.plugin_group + ".clear")) {
                    commandSender.sendMessage(TranslatableText.create("command.without_permission"));
                    return true;
                }

                Player player = Bukkit.getPlayer(strings[1]);
                if (player != null && DeadManager.isDead(player)) {
                    DeadManager.clear(Bukkit.getPlayer(strings[1]));
                    commandSender.sendMessage(TranslatableText.create("command.clear.cleared"));
                } else {
                    commandSender.sendMessage(TranslatableText.create("command.non_player"));
                }
                return true;
            }
        }

        return false;
    }
}
