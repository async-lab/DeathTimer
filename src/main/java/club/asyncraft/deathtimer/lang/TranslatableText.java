package club.asyncraft.deathtimer.lang;

import club.asyncraft.deathtimer.DeathTimer;
import club.asyncraft.deathtimer.util.Reference;
import club.asyncraft.deathtimer.util.YamlMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Paths;

public class TranslatableText {

    public static TranslatableText instance;

    public YamlMap langMap = new YamlMap();

    public static void init(String configLang) throws Exception {
        TranslatableText.instance = new TranslatableText();

        //导出所有语言文件
        for (String lang : Reference.plugin_langs) {
            String langRelativePath = "locals/" + lang + ".yml";
            if (!new File(DeathTimer.instance.getDataFolder(), langRelativePath).exists())
                DeathTimer.instance.saveResource(langRelativePath, false);
        }

        String langFilePath = Paths.get(DeathTimer.instance.getDataFolder().getAbsolutePath(), "locals", configLang + ".yml").toString();
        File langFile = new File(langFilePath);
        if (!langFile.exists()) {
            throw new Exception("Lang file is not found!");
        }

        TranslatableText.instance.langMap = new YamlMap(YamlConfiguration.loadConfiguration(langFile));
    }

    public static String create(String key) {
        YamlMap.Node node = TranslatableText.instance.langMap.get(key);
        if (node == null) {
            return key;
        }

        String translatedText = (String) node.get();
        return translatedText;
    }
}
