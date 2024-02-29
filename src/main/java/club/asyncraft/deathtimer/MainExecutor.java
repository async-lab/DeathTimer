package club.asyncraft.deathtimer;

import club.asyncraft.deathtimer.util.DeadManager;
import club.asyncraft.deathtimer.util.Reference;
import club.asyncraft.papercutter.api.executor.CutterExecutor;
import club.asyncraft.papercutter.api.executor.CutterExecutorSection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class MainExecutor extends CutterExecutor {
    public MainExecutor() {
        super(Reference.plugin_id);
        this.getRootSection()
                .setHandler((sender, command, label, args) -> sender.sendMessage(DeathTimer.translatableContext.translate("command.help")))
                .addStaticChildren(
                        new CutterExecutorSection("reload")
                                .setPermissionName(Reference.plugin_group + ".reload")
                                .setHandler(this::onReload),
                        new CutterExecutorSection("revive")
                                .setHandler(this::onRevive),
                        new CutterExecutorSection("clear")
                                .setPermissionName(Reference.plugin_group + ".clear")
                                .setHandler((sender, command, label, args) -> sender.sendMessage(DeathTimer.translatableContext.translate("command.clear.usage")))
                                .addChildrenSuppliers((sender, command, alias, args) ->
                                        Bukkit.getOnlinePlayers().stream()
                                                .filter(player -> player.getName().startsWith(args[1]) && DeadManager.isDead(player))
                                                .map(player -> new CutterExecutorSection(player.getName()).setHandler(this::onClear))
                                                .collect(Collectors.toList()))
                );
    }

    private void onReload(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!DeathTimer.instance.initConfig()) {
            DeathTimer.disable();
        }
        commandSender.sendMessage(DeathTimer.translatableContext.translate("command.reload.reloaded"));
    }

    private void onRevive(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(DeathTimer.translatableContext.translate("command.non_console"));
        } else {
            Player player = (Player) commandSender;
            if (player.getGameMode() == GameMode.SPECTATOR && DeadManager.getRemainingTime(player.getUniqueId().toString()) == 0) {
                player.setGameMode(GameMode.SURVIVAL);
                DeadManager.revive(player);
                player.sendMessage(DeathTimer.translatableContext.translate("command.revive.revived"));
            } else {
                player.sendMessage(DeathTimer.translatableContext.translate("command.revive.time_left"));
            }
        }
    }

    private void onClear(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = Bukkit.getPlayer(args[1]);
        if (player != null && DeadManager.isDead(player)) {
            DeadManager.clear(Bukkit.getPlayer(args[1]));
            commandSender.sendMessage(DeathTimer.translatableContext.translate("command.clear.cleared"));
        } else {
            commandSender.sendMessage(DeathTimer.translatableContext.translate("command.non_player"));
        }
    }
}
