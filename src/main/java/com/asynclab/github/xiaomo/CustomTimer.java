package com.asynclab.github.xiaomo;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Calendar;
import java.util.List;

public class CustomTimer implements Runnable{

    private BukkitTask bukkitTask;
    private String playerName;

    public Calendar startTime;

    public CustomTimer(String playerName) {
        this.playerName = playerName;
    }

    public CustomTimer() {
    }

    public CustomTimer(String playerName,Calendar startTime) {
        this.startTime = startTime;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        excuteCommands();
        deathtimer.deadPlayers.remove(playerName);
    }

    /**
     *  计时完毕后执行代码
     * */
    public void excuteCommands() {
        List<String> commands = deathtimer.commands;
        deathtimer d = deathtimer.instance;
        Player player = d.getServer().getPlayer(playerName);
        respawn();
        for (String cmd : commands) {
            String papiCmd;
            if (deathtimer.hasPapi) {
                papiCmd = PlaceholderAPI.setPlaceholders(player,cmd);
            }  else  {
                papiCmd = cmd;
            }
            d.getServer().dispatchCommand(d.getServer().getConsoleSender(),papiCmd);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    * 开始计时
    * */
    public void startTask() {
        deathtimer d = deathtimer.instance;
        if (startTime == null) {
            startTime = Calendar.getInstance();
        }
        bukkitTask = d.getServer().getScheduler().runTaskLater(d,this, deathtimer.timeAfterDeath);
    }

    public void startTask(long ticks) {
        deathtimer d = deathtimer.instance;
        bukkitTask = d.getServer().getScheduler().runTaskLater(d,this,ticks);
    }

    /*
    * 取消计时
    * */
    public void cancelTask() {
        bukkitTask.cancel();
    }

    /*
    * 玩家为可复活状态
    * */
    public void respawn() {
        Player player = deathtimer.instance.getServer().getPlayer(playerName);
        if (deathtimer.respawnMode) {
            AliveCommand.canRespawnPlayers.add(playerName);
            informPlayer(player);
        }
    }

    public static void informPlayer(Player player) {
        player.sendTitle(ChatColor.GREEN + "你现在可以重生了",ChatColor.RED + "输入/alive复活",10,70,20);
        player.sendMessage(ChatColor.GREEN + "你现在可以重生了,找到合适位置后输入" + ChatColor.YELLOW + " /alive " + ChatColor.GREEN + "重生");
    }

}
