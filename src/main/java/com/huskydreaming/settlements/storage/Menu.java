package com.huskydreaming.settlements.storage;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Menu implements Parseable {
    CONFIRMATION_YES_TITLE("&a&lYes"),
    CONFIRMATION_NO_TITLE("&c&lNo"),

    // Settlement Menu
    SETTLEMENT_TITLE("&e{0}. {1}"),
    SETTLEMENT_LORE(Arrays.asList(
            "",
            "&7Owner: &f{0}",
            "&7Members: &b{1}&3/&b{2}",
            "&7Claims: &a{3}&2/&a{4}",
            "&7Roles: &e{5}&6/&e{6}"
    )),
    SETTLEMENT_CITIZENS_TITLE("&aMembers"),
    SETTLEMENT_CITIZENS_LORE(Collections.singletonList("&7Click to edit Members.")),
    SETTLEMENT_ROLES_TITLE("&aRoles"),
    SETTLEMENT_ROLES_LORE(Collections.singletonList("&7Click to edit Roles.")),
    SETTLEMENT_ROLE_EDIT_TITLE("&a{0}. {1} &7{2}"),
    SETTLEMENT_ROLE_EDIT_DEFAULT("(Default)"),
    SETTLEMENT_ROLE_EDIT_LORE(Collections.singletonList("&7Click to edit Role.")),
    SETTLEMENT_CLAIMS_TITLE("&aClaims"),
    SETTLEMENT_CLAIMS_LORE(Collections.singletonList("&7Click to view claims.")),
    SETTLEMENT_FLAGS_TITLE("&aFlags"),
    SETTLEMENT_FLAGS_LORE(List.of("&7Click to adjust flags.", "", "&f&oFlags currently don't function")),
    SETTLEMENT_SPAWN_TITLE("&aSpawn"),
    SETTLEMENT_SPAWN_LORE(Arrays.asList(
            "&7Left-Click to teleport",
            "&7Right-Click to set spawn"
    )),
    SETTLEMENT_DISBAND_TITLE("&cDisband"),
    SETTLEMENT_DISBAND_LORE(Collections.singletonList("&7Click to disband settlement.")),
    SETTLEMENT_INFO_TITLE("&aSettlement Info"),
    SETTLEMENT_INFO_LORE(List.of(
            "&7&O{0}",
            "",
            "&7Owner: &f{1}",
            "&7Members: &b{2}&3/&b{3}",
            "&7Claims: &a{4}&2/&a{5}",
            "&7Roles: &e{6}&6/&e{7}"
    )),
    //Members Menu
    MEMBER_SET_OWNER_TITLE("&aSet Owner"),
    MEMBER_SET_OWNER_LORE(Collections.singletonList("&7Click to set owner")),
    MEMBER_SET_ROLE_TITLE("&aSet Role"),
    MEMBER_SET_ROLE_LORE(List.of(
            "&7Current Role: &f{0}. {1}",
            "",
            "&7Left-Click > Promote",
            "&7Right-Click > Demote"
    )),
    MEMBER_KICK_TITLE("&aKick"),
    MEMBER_KICK_LORE(Collections.singletonList("&7Remove from settlement.")),
    // Citizens Menu
    MEMBERS_TITLE("&e{0}. {1}"),
    MEMBERS_LORE(Arrays.asList(
            "&f{0} &7| {1}",
            "&7Last Online: &f{2}",
            "",
            "&7Click to edit player."
    )),
    MEMBERS_STATUS_OFFLINE("&cOffline"),
    MEMBERS_STATUS_ONLINE("&aOnline"),

    DISBAND_TITLE("Disband Settlement?"),

    // Claims Menu
    CLAIMS_TITLE("&a{0}, {1}"),
    CLAIMS_LORE(List.of("&7Click to teleport.")),

    // Roles Menu
    ROLE_DELETE_TITLE("&cDelete"),
    ROLE_DELETE_LORE(Collections.singletonList("&7Click to delete role.")),
    ROLE_DEFAULT_TITLE("&bDefault"),
    ROLE_DEFAULT_LORE(Collections.singletonList("&7Set role as default."));

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

    @Override
    public String prefix(Object... objects) {
        return null;
    }

    @Nullable
    public String parse() {
        String message = menuConfiguration.getString(toString(), def);
        if (message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = menuConfiguration.getList(toString(), list);
        if (objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @NotNull
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Menu.menuConfiguration = configuration;
    }
}