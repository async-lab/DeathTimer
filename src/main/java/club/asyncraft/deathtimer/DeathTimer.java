package club.asyncraft.deathtimer;

import club.asyncraft.deathtimer.command.AliveCommand;
import club.asyncraft.deathtimer.command.MainCommand;
import club.asyncraft.deathtimer.event.CustomListener;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class DeathTimer extends JavaPlugin {

    public static DeathTimer instance;

    public int cooldown;
    public List<String> commands;

    public String timeUnit;
    public boolean hasPapi;


    @Override
    public void onEnable() {
        this.getLogger().info("Loading");
        DeathTimer.instance = this;

        AliveCommand.canRespawnPlayers = new ArrayList<>();
        if (!initConfig()) {
            return;
        }


        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.hasPapi = true;
            getLogger().info("PlaceholderAPI Hooked");
        } else {
            this.hasPapi = false;
            getLogger().info("PlaceholderAPI Not Found");
        }

        for (Permission permission : Reference.plugin_permissions) {
            Bukkit.getPluginManager().addPermission(permission);
        }

        this.getServer().getPluginCommand("DeathTimer").setExecutor(new MainCommand());
        this.getServer().getPluginManager().registerEvents(new CustomListener(), instance);
        this.getLogger().info("Loaded");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling");
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public boolean initConfig() {
        try {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
            this.reloadConfig();

            //检查config中配置的lang是否存在
            String configLang = this.getConfig().getString("lang");

            TranslatableText.init(configLang);

            String unit = this.getConfig().getString("unit");
            this.timeUnit = unit;
            int time = this.getConfig().getInt("cooldown");

            switch (unit.toUpperCase()) {
                case "MINUTE":
                    this.cooldown = time * 60;
                    break;
                case "HOUR":
                    this.cooldown = time * 60 * 60;
                    break;
                case "SECOND":
                    this.cooldown = time;
                    break;
                default:
                    this.getLogger().info("配置文件错误,卸载插件...");
                    this.getServer().getScheduler().cancelTasks(this);
                    Bukkit.getPluginManager().disablePlugin(this);
                    return false;
            }

            this.commands = getConfig().getStringList("commands");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().info("Plugin is disabled.");
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        }
    }

}
