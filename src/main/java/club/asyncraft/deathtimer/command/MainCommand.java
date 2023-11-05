package club.asyncraft.deathtimer.command;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.DeadManager;
import club.asyncraft.deathtimer.util.PermissionWrapper;
import club.asyncraft.deathtimer.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements TabExecutor {

    public static List<String> children = Arrays.asList("help", "reload", "revive", "clear");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(TranslatableText.create("command.help"));
        } else switch (args[0].toLowerCase()) {
            case "help":
                commandSender.sendMessage(TranslatableText.create("command.help"));
                break;
            case "reload":
                this.onReload(commandSender);
                break;
            case "revive":
                this.onRevive(commandSender);
                break;
            case "clear":
                this.onClear(commandSender, args);
                break;
            default:
                commandSender.sendMessage(TranslatableText.create("command.wrong_usage"));
                break;
        }

        return true;
    }

    private void onReload(@NotNull CommandSender commandSender) {
        if (!commandSender.hasPermission(Reference.plugin_group + ".reload")) {
            commandSender.sendMessage(TranslatableText.create("command.without_permission"));
            return;
        }

        if (!DeathTimer.instance.initConfig()) {
            DeathTimer.disable();
        }
        commandSender.sendMessage(TranslatableText.create("command.reload.reloaded"));
    }

    private void onRevive(@NotNull CommandSender commandSender) {
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
    }

    private void onClear(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (!commandSender.hasPermission(Reference.plugin_group + ".clear")) {
            commandSender.sendMessage(TranslatableText.create("command.without_permission"));
            return;
        } else if (args.length != 2) {
            commandSender.sendMessage(TranslatableText.create("command.clear.usage"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player != null && DeadManager.isDead(player)) {
            DeadManager.clear(Bukkit.getPlayer(args[1]));
            commandSender.sendMessage(TranslatableText.create("command.clear.cleared"));
        } else {
            commandSender.sendMessage(TranslatableText.create("command.non_player"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> hint = new ArrayList<>();

        if (args.length == 1) {
            MainCommand.children.stream()
                    .filter(
                            child ->
                                    child.startsWith(args[0].toLowerCase()) && new PermissionWrapper(Reference.plugin_group + "." + child).hasPermission(commandSender)
                    ).forEach(hint::add);
        } else switch (args[0].toLowerCase()) {
            case "clear":
                Bukkit.getOnlinePlayers().stream()
                        .filter(
                                player -> player.getName().startsWith(args[1]) && DeadManager.isDead(player)
                        ).forEach(player -> hint.add(player.getName()));
                break;
            default:
                break;
        }

        return hint;
    }
}
