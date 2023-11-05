package club.asyncraft.deathtimer.event;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.DeadManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CustomListener implements Listener {

    private static Map<String, BukkitTask> taskMap = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
//        CustomTimer task = new CustomTimer(player);
//        DeathTimer.deadPlayers.put(player, task);
//        task.startTask();
        DeadManager.die(event.getEntity());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
//        String player = event.getPlayer().getName();
//        CustomTimer task = DeathTimer.deadPlayers.get(player);
//        if (task != null) {
//            task.cancelTask();
//            DeathTimer.deadPlayers.remove(player);
//        }
        BukkitTask task = CustomListener.taskMap.get(event.getPlayer().getUniqueId().toString());
        if (task != null) {
            task.cancel();
            CustomListener.taskMap.remove(event.getPlayer().getUniqueId().toString());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
//        CustomTimer timer = DeathTimer.offlinePlayer.get(player.getName());
//        if (timer != null) {
//            Calendar time = timer.startTime;
//            time.add(Calendar.MILLISECOND, (int) DeathTimer.timeAfterDeath * 50);
//            if (time.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
//                timer.excuteCommands();
//            } else {
//                long tick = (time.getTimeInMillis() - timer.startTime.getTimeInMillis()) - (Calendar.getInstance().getTimeInMillis() - timer.startTime.getTimeInMillis());
//                tick /= 50;
//                DeathTimer.deadPlayers.put(player.getName(), timer);
//                timer.startTask(tick);
//            }
//            DeathTimer.offlinePlayer.remove(player.getName());
//        }
//        if (DeathTimer.respawnMode && AliveCommand.canRespawnPlayers.contains(player.getName())) {
//            CustomTimer.informPlayer(player);
//        }
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(DeathTimer.instance, () -> {
            Player player = event.getPlayer();
            if (!player.isOnline()) {
                CustomListener.taskMap.get(player.getUniqueId().toString()).cancel();
                return;
            }

            if (player.getGameMode() == GameMode.SPECTATOR) {
                if (DeadManager.getRemainingTime(player.getUniqueId().toString()) == 0) {
                    event.getPlayer().sendActionBar(TranslatableText.create("actionbar.cooldown.end"));
                } else {
                    event.getPlayer().sendActionBar("你还有" + DeadManager.getRemainingTime(event.getPlayer().getUniqueId().toString()) + "秒复活");
                }
            } else if (!player.isDead()) {
                DeadManager.revive(player);
            }
        }, 0L, 20L);

        CustomListener.taskMap.put(event.getPlayer().getUniqueId().toString(), task);
    }

}
