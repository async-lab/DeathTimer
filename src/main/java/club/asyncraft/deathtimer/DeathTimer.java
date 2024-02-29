package club.asyncraft.deathtimer;

import club.asyncraft.deathtimer.event.CustomListener;
import club.asyncraft.deathtimer.util.Reference;
import club.asyncraft.papercutter.api.i18n.TranslatableContext;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public final class DeathTimer extends JavaPlugin {

    public static DeathTimer instance;

    public static TranslatableContext translatableContext;

    public static MainExecutor mainExecutor;

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
            this.getLogger().info(DeathTimer.translatableContext.translate("debug.placeholder_api.hooked"));
        } else {
            this.hasPapi = false;
            this.getLogger().info(DeathTimer.translatableContext.translate("debug.placeholder_api.not_found"));
        }

        DeathTimer.mainExecutor = new MainExecutor();

        for (Permission permission : Reference.plugin_permissions) {
            Bukkit.getPluginManager().addPermission(permission);
        }

        Objects.requireNonNull(this.getServer().getPluginCommand("DeathTimer")).setExecutor(DeathTimer.mainExecutor);
        Objects.requireNonNull(this.getServer().getPluginCommand("DeathTimer")).setTabCompleter(DeathTimer.mainExecutor);

        this.getServer().getPluginManager().registerEvents(new CustomListener(), instance);
        this.getLogger().info(DeathTimer.translatableContext.translate("debug.loaded"));
    }

    @Override
    public void onDisable() {
        this.getLogger().info(DeathTimer.translatableContext.translate("debug.disabled"));
    }

    public boolean initConfig() {
        try {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
            this.reloadConfig();

            //检查config中配置的lang是否存在
            String configLang = this.getConfig().getString("lang");

            //初始化i18n
            translatableContext = new TranslatableContext(this, Reference.plugin_langs, configLang);

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
                    Bukkit.getLogger().info(DeathTimer.translatableContext.translate("debug.config_error"));
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
