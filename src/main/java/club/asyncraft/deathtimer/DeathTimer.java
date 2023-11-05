package club.asyncraft.deathtimer;

import club.asyncraft.deathtimer.command.MainCommand;
import club.asyncraft.deathtimer.event.CustomListener;
import club.asyncraft.deathtimer.lang.TranslatableText;
import club.asyncraft.deathtimer.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class DeathTimer extends JavaPlugin {

    public static DeathTimer instance;

    public int cooldown;
    public List<String> commands;

    public String timeUnit;
    public boolean hasPapi;


    @Override
    public void onEnable() {
        DeathTimer.instance = this;

        if (!initConfig()) {
            DeathTimer.disable();
            return;
        }

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.hasPapi = true;
            this.getLogger().info(TranslatableText.create("debug.placeholder_api.hooked"));
        } else {
            this.hasPapi = false;
            this.getLogger().info(TranslatableText.create("debug.placeholder_api.not_found"));
        }

        for (Permission permission : Reference.plugin_permissions) {
            Bukkit.getPluginManager().addPermission(permission);
        }

        this.getServer().getPluginCommand("DeathTimer").setExecutor(new MainCommand());
        this.getServer().getPluginCommand("DeathTimer").setTabCompleter(new MainCommand());

        this.getServer().getPluginManager().registerEvents(new CustomListener(), instance);
        this.getLogger().info(TranslatableText.create("debug.loaded"));
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TranslatableText.create("debug.disabled"));
    }

    public boolean initConfig() {
        try {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
            this.reloadConfig();

            //检查config中配置的lang是否存在
            String configLang = this.getConfig().getString("lang");

            //初始化i18n
            TranslatableText.init(configLang);

            this.timeUnit = this.getConfig().getString("unit");
            int time = this.getConfig().getInt("cooldown");

            switch (this.timeUnit.toUpperCase()) {
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
                    Bukkit.getLogger().info(TranslatableText.create("debug.config_error"));
                    Bukkit.getScheduler().cancelTasks(this);
                    throw new RuntimeException("Config error");
            }

            this.commands = getConfig().getStringList("commands");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void disable() {
        Bukkit.getPluginManager().disablePlugin(DeathTimer.instance);
    }

}
