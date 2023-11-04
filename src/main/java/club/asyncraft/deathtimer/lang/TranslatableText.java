package club.asyncraft.deathtimer.lang;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.util.Reference;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TranslatableText {

    public static TranslatableText instance;

    public Map<String, String> langMap = new HashMap<>();

    public static void init(String configLang) throws Exception {
        TranslatableText.instance = new TranslatableText();

        //导出所有语言文件
        for (String lang : Reference.plugin_langs) {
            if (!new File(DeathTimer.instance.getDataFolder(), "locals/" + lang + ".yml").exists())
                DeathTimer.instance.saveResource("locals/" + lang + ".yml", false);
        }

        String langFilePath = Paths.get(DeathTimer.instance.getDataFolder().getAbsolutePath(), "locals", configLang + ".yml").toString();
        File langFile = new File(langFilePath);
        if (!langFile.exists()) {
            throw new Exception("Lang file is not found!");
        }

        TranslatableText.instance.langMap = TranslatableText.loadLangFile(YamlConfiguration.loadConfiguration(langFile));
    }

    public static Map<String, String> loadLangFile(FileConfiguration config) {
        Map<String, String> resultMap = new HashMap<>();
        traverseSection(config, "", resultMap);
        return resultMap;
    }

    private static void traverseSection(ConfigurationSection section, String pathPrefix, Map<String, String> resultMap) {
        for (String key : section.getKeys(false)) {
            String currentPath = pathPrefix.isEmpty() ? key : pathPrefix + "." + key;
            if (section.isConfigurationSection(key)) {
                traverseSection(section.getConfigurationSection(key), currentPath, resultMap);
            } else {
                resultMap.put(currentPath, section.getString(key));
            }
        }
    }

    public static String create(String key) {
        String translatedText = TranslatableText.instance.langMap.get(key);
        return translatedText == null ? key : translatedText;
    }
}
