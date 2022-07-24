package com.huskydreaming.settlements.utilities;

import com.google.common.base.Functions;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Menu {
    CITIZENS_TITLE("&aCitizens"),
    CITIZENS_LORE(Collections.singletonList("&7Click to edit Citizens.")),
    ROLES_TITLE("&aRoles"),
    ROLES_LORE(Collections.singletonList("&7Click to edit Roles."));

    private final String def;
    private final List<String> list;
    private static FileConfiguration menuConfiguration;

    Menu(String def) {
        this.def = def;
        this.list = null;
    }

    Menu(List<String> list) {
        this.list = list;
        this.def = null;
    }

    public String parse() {
        return menuConfiguration.getString(toString(), def);
    }

    public List<String> parseList() {
        List<?> objects = menuConfiguration.getList(toString(), list);
        if(objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Menu.menuConfiguration = configuration;
    }
}
