package club.asyncraft.deathtimer.util;


import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

public class Reference {
    public static String plugin_name = "DeathTimer";
    public static String plugin_id = "deathtimer";

    public static String plugin_group = "club.asyncraft.deathtimer";

    public static String[] plugin_langs = {
            "zh_CN"
    };

    public static List<Permission> plugin_permissions = Arrays.asList(
//            new PermissionWrapper(plugin_group + ".reload").setDefaultChain(PermissionDefault.OP)
    );
}
