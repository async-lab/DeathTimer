package com.asynclab.github.xiaomo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Calendar;

public class CustomListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String player = event.getEntity().getName();
        if (deathtimer.deadPlayers.get(player) != null) {
            return;
        }
        CustomTimer task = new CustomTimer(player);
        deathtimer.deadPlayers.put(player,task);
        task.startTask();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String player = event.getPlayer().getName();
        CustomTimer task = deathtimer.deadPlayers.get(player);
        if (task != null) {
            deathtimer.offlinePlayer.put(player,task);
            task.cancelTask();
            deathtimer.deadPlayers.remove(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CustomTimer timer = deathtimer.offlinePlayer.get(player.getName());
        if (timer != null) {
            Calendar time = timer.startTime;
            time.add(Calendar.MILLISECOND, (int) deathtimer.timeAfterDeath * 50);
            if (time.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                timer.excuteCommands();
            } else {
                long tick = (time.getTimeInMillis() - timer.startTime.getTimeInMillis()) - (Calendar.getInstance().getTimeInMillis() - timer.startTime.getTimeInMillis());
                tick /= 50;
                deathtimer.deadPlayers.put(player.getName(),timer);
                timer.startTask(tick);
            }
            deathtimer.offlinePlayer.remove(player.getName());
        }
        if (deathtimer.respawnMode && AliveCommand.canRespawnPlayers.contains(player.getName())) {
            CustomTimer.informPlayer(player);
        }
    }


}
