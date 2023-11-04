package com.asynclab.github.xiaomo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class deathtimer extends JavaPlugin {

    public static deathtimer instance;

    public static long timeAfterDeath;
    public static List<String> commands;
    //在线的死亡玩家
    public static Map<String, CustomTimer> deadPlayers;
    //离线的死亡玩家
    public static Map<String,CustomTimer> offlinePlayer;
    public static String timeUnit;
    public static boolean hasPapi;

    public static boolean respawnMode = false;

    public static File dataFile;
    public static FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        getLogger().info("Loading");
        instance = this;
        deadPlayers = new HashMap<>();
        offlinePlayer = new HashMap<>();
        AliveCommand.canRespawnPlayers = new ArrayList<>();
        if (!initConfig()) {
            return;
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            hasPapi = true;
            getLogger().info("PlaceholderAPI Hooked");
        } else {
            hasPapi = false;
            getLogger().info("PlaceholderAPI Not Found");
        }
        getServer().getPluginCommand("DeathTimer").setExecutor(new MainCommands());
        getServer().getPluginManager().registerEvents(new CustomListener(),instance);
        getServer().getPluginCommand("alive").setExecutor(new AliveCommand());
        getLogger().info("Loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling");
        writeData();
        writeDeadConfig();
    }

    /*
    * 初始化配置文件
    * */
    public boolean initConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        deathtimer.dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        deathtimer.dataConfig = YamlConfiguration.loadConfiguration(deathtimer.dataFile);
        reloadConfig();
        String unit = getConfig().getString("unit");
        deathtimer.timeUnit = unit;
        deathtimer.respawnMode = getConfig().getBoolean("mode");
        long time = getConfig().getLong("time");
        if ("MINUTE".equalsIgnoreCase(unit)) {
            timeAfterDeath = time*1200;
        } else if ("HOUR".equalsIgnoreCase(unit)) {
            timeAfterDeath = time*60*1200;
        } else if ("SECOND".equalsIgnoreCase(unit)) {
            timeAfterDeath = time*20;
        } else {
            getLogger().info("配置文件错误,卸载插件...");
            getServer().getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        commands = getConfig().getStringList("commands");
        try {
            readData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readDeadConfig();
        return true;
    }
    /**
    * 读取data.yml
    * */
    public void readData() throws ParseException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String name : dataConfig.getKeys(false)) {
            Date date = dateFormat.parse(dataConfig.getString(name + ".time"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            offlinePlayer.put(name,new CustomTimer(name,calendar));
            dataConfig.set(name,null);
        }
        dataConfig.save(dataFile);
    }

    /**
    * 写入data.yml
    * */
    public void writeData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String name : deadPlayers.keySet()) {
            CustomTimer timer = deadPlayers.get(name);
            dataConfig.set(name + ".time",simpleDateFormat.format(timer.startTime.getTime()));
        }
        for (String name : offlinePlayer.keySet()) {
            CustomTimer timer = offlinePlayer.get(name);
            dataConfig.set(name + ".time",simpleDateFormat.format(timer.startTime.getTime()));
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 读取deadPlayers.yml
    * */
    public void readDeadConfig() {
        File deadFile = new File(getDataFolder(),"deadPlayers.yml");
        if (!deadFile.exists()) {
            saveResource("deadPlayers.yml", false);
        }
        FileConfiguration deadConfig = YamlConfiguration.loadConfiguration(deadFile);
        List<String> list = deadConfig.getStringList("data");
        for (String name : list) {
            AliveCommand.canRespawnPlayers.add(name);
        }
        deadConfig.set("data",null);
        try {
            deadConfig.save(deadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 写入deadPlayers.yml
    * */
    public void writeDeadConfig() {
        File deadFile = new File(getDataFolder(),"deadPlayers.yml");
        FileConfiguration deadConfig = YamlConfiguration.loadConfiguration(deadFile);
        deadConfig.set("data",AliveCommand.canRespawnPlayers);
        try {
            deadConfig.save(deadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
