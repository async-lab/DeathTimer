package club.asyncraft.deathtimer.util;

import club.asyncraft.deathtimer.DeathTimer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DeadManager {

    public static boolean check(String uuid) {
        return DeadManager.getRemainingTime(uuid) == 0;
    }

    public static int getRemainingTime(String uuid) {
        return (int) DeadManager.handle(map -> {
            if (map.containsKey(uuid)) {
                int deathDate = map.get(uuid);
                int nowDate = DeadManager.getTimeNow();
                if (nowDate - deathDate < DeathTimer.cooldown) {
                    return DeathTimer.cooldown - (nowDate - deathDate);
                }
            }

            return 0;
        });
    }

    public static void die(String uuid) {
        DeadManager.handle(map -> {
            map.put(uuid, DeadManager.getTimeNow());
            return true;
        });
    }

    public static void revive(String uuid) {
        DeadManager.handle(map -> {
            map.remove(uuid);
            return true;
        });
    }

    public static Object handle(Function<Map<String, Integer>, Object> function) {
        FileConfiguration dead = getDead();

        Map<String, Integer> deadMap = writeData(dead);

        Object result = function.apply(deadMap);

        dead = readData(deadMap);

        try {
            dead.save(getDeadFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static int getTimeNow() {
        return (int) (new Date().getTime() / 1000);
    }

    //写
    private static FileConfiguration readData(Map<String, Integer> map) {
        FileConfiguration dead = new YamlConfiguration();

        for (String uuid : map.keySet()) {
            int date = map.get(uuid);
            dead.set(uuid, date);
        }

        return dead;
    }

    //读
    private static Map<String, Integer> writeData(FileConfiguration dead) {
        Map<String, Integer> map = new HashMap<>();

        for (String uuid : dead.getKeys(false)) {
            int date = dead.getInt(uuid);
            map.put(uuid, date);
        }

        return map;
    }

    private static File getDeadFile() {
        return new File(DeathTimer.instance.getDataFolder(), "dead.yml");
    }

    private static FileConfiguration getDead() {
        if (!getDeadFile().exists()) {
            DeathTimer.instance.saveResource("dead.yml", false);
        }
        return YamlConfiguration.loadConfiguration(getDeadFile());
    }
}
