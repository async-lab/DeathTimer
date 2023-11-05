package club.asyncraft.deathtimer.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AliveCommand implements CommandExecutor {

//    public static List<String> canRespawnPlayers;

    /*
     * /alive指令
     * */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        if (!(commandSender instanceof Player)) {
//            return true;
//        }
//        if (strings.length == 0) {
//            if (canRespawnPlayers.contains(commandSender.getName())) {
//                Player player = (Player) commandSender;
//                player.setGameMode(GameMode.SURVIVAL);
//                canRespawnPlayers.remove(player.getName());
//                player.sendMessage("已复活");
//                return true;
//            } else {
//                commandSender.sendMessage(ChatColor.RED + "未到时间或无需重生");
//                return true;
//            }
//        }
//        if (strings.length == 2) {
//            if ("ok".equalsIgnoreCase(strings[0])) {
//                if (!hasPermission(commandSender)) {
//                    commandSender.sendMessage("你没有权限");
//                    return true;
//                }
//                Player player = DeathTimer.instance.getServer().getPlayer(strings[1]);
//                if (player == null) {
//                    commandSender.sendMessage("Invalid player id");
//                    return true;
//                }
//                CustomTimer customTimer = DeathTimer.deadPlayers.get(player.getName());
//                if (customTimer == null || canRespawnPlayers.contains(player.getName())) {
//                    commandSender.sendMessage("This player does not need to be respawned");
//                    return true;
//                }
//                customTimer.respawn();
//                DeathTimer.deadPlayers.remove(player.getName());
//                commandSender.sendMessage("OK!");
//                return true;
//            }
//        }
        return false;
    }

    public boolean hasPermission(CommandSender sender) {
//        if (sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("deathtimer.ok")) {
//            return true;
//        }
        return false;
    }

}
