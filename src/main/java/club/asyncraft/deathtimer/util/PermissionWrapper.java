package club.asyncraft.deathtimer.util;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;

public class PermissionWrapper extends Permission {

    public PermissionWrapper(String name) {
        this(name, null, null, null);
    }

    public PermissionWrapper(String name, String description) {
        this(name, description, null, null);
    }

    public PermissionWrapper(String name, PermissionDefault defaultValue) {
        this(name, null, defaultValue, null);
    }

    public PermissionWrapper(String name, String description, PermissionDefault defaultValue) {
        this(name, description, defaultValue, null);
    }

    public PermissionWrapper(String name, Map<String, Boolean> children) {
        this(name, null, null, children);
    }

    public PermissionWrapper(String name, String description, Map<String, Boolean> children) {
        this(name, description, null, children);
    }

    public PermissionWrapper(String name, PermissionDefault defaultValue, Map<String, Boolean> children) {
        this(name, null, defaultValue, children);
    }

    public PermissionWrapper(String name, String description, PermissionDefault defaultValue, Map<String, Boolean> children) {
        super(name, description, defaultValue, children);
    }

    public PermissionWrapper setDefaultChain(PermissionDefault defaultValue) {
        super.setDefault(defaultValue);
        return this;
    }

    public PermissionWrapper setDescriptionChain(String description) {
        super.setDescription(description);
        return this;
    }

    public PermissionWrapper addParentChain(String name, boolean value) {
        super.addParent(name, value);
        return this;
    }

}
