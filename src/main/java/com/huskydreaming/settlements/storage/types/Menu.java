package com.huskydreaming.settlements.storage.types;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Menu implements Parseable {

    // ADMIN Menu
    ADMIN_DISABLED_WORLD_TITLE("&e{0}"),
    ADMIN_DISABLED_WORLD_LORE(List.of("&7This world is not available")),
    ADMIN_DISABLED_WORLDS_TITLE("&eDisabled Worlds"),
    ADMIN_DISABLED_WORLDS_LORE(List.of("&7{0}", "", "&fClick to edit worlds")),

    ADMIN_NOTIFICATION_TITLE("&eNotifications ⚠"),
    ADMIN_NOTIFICATION_LORE(List.of(
            "&7Current Notification: &b{0}",
            "",
            "&fClick to select notification type"
    )),

    CONFIRMATION_YES_TITLE("&a&lYes"),
    CONFIRMATION_NO_TITLE("&c&lNo"),

    // Settlement Menu
    SETTLEMENT_TITLE("&e{0}. {1}"),
    SETTLEMENT_LORE(List.of(
            "",
            "&7Owner: &f{0}",
            "&7Members: &b{1}&3/&b{2}",
            "&7Claims: &a{3}&2/&a{4}",
            "&7Roles: &e{5}&6/&e{6}"
    )),
    SETTLEMENT_MEMBERS_TITLE("&aMembers"),
    SETTLEMENT_MEMBERS_LORE(List.of("&7Displays all members of the settlement", "", "&fClick to view members")),
    SETTLEMENT_ROLES_TITLE("&aRoles"),
    SETTLEMENT_ROLES_LORE(List.of("&7Shows all available roles", "", "&fClick to view roles")),
    SETTLEMENT_ROLE_EDIT_TITLE("&a{0}. {1} &7{2}"),
    SETTLEMENT_ROLE_EDIT_DEFAULT("(Default)"),
    SETTLEMENT_ROLE_EDIT_LORE(List.of("&7Role part of your settlement", "", "&fClick to edit role")),
    SETTLEMENT_CLAIMS_TITLE("&aClaims"),
    SETTLEMENT_CLAIMS_LORE(List.of("&7Shows All available claims", "", "&fClick to edit claims")),
    SETTLEMENT_FLAGS_TITLE("&aFlags"),
    SETTLEMENT_FLAGS_LORE(List.of("&7Click to adjust flags.", "", "&f&oFlags currently don't function")),
    SETTLEMENT_SPAWN_TITLE("&aSpawn"),
    SETTLEMENT_SPAWN_LORE(List.of("&7Spawn is set at your location", "", "&fClick to set spawn")),
    SETTLEMENT_DISBAND_TITLE("&cDisband"),
    SETTLEMENT_DISBAND_LORE(List.of("&7This will permenantly delete the settlement", "", "&fClick to disband settlement")),
    SETTLEMENT_INFO_TITLE("&aSettlement Info"),
    SETTLEMENT_INFO_LORE(List.of(
            "&7&O{0}",
            "",
            "&7Owner: &f{1}",
            "",
            "&7Members: &b{2}&3/&b{3}",
            "&7Claims: &a{4}&2/&a{5}",
            "&7Roles: &e{6}&6/&e{7}"
    )),
    //Members Menu
    MEMBER_SET_OWNER_TITLE("&aSet Owner"),
    MEMBER_SET_OWNER_LORE(List.of("&7Current owner is {0}", "", "&fClick to set owner")),
    MEMBER_SET_ROLE_TITLE("&aSet Role"),
    MEMBER_SET_ROLE_LORE(List.of(
            "&7Current Role: &b{0}. {1}",
            "",
            "&fLeft-Click > Promote",
            "&fRight-Click > Demote"
    )),
    MEMBER_TELEPORT_TITLE("&aTeleport"),
    MEMBER_TELEPORT_LORE(List.of("&7Teleports you to players location", "","&fClick to teleport to player")),
    MEMBER_KICK_TITLE("&aKick"),
    MEMBER_KICK_LORE(List.of("&7Kicks members from the settlement","", "&fClick to kick member")),
    // Citizens Menu
    MEMBERS_TITLE("&e{0}. {1}"),
    MEMBERS_LORE(List.of(
            "&7Last Online {2}",
            "",
            "&f{0} &7| {1}",
            "",
            "&fClick to edit player."
    )),

    TRUSTED_TITLE("&e{0}. {1} &f[Trusted]"),
    TRUSTED_LORE(List.of("&7A trusted member", "", "&fClick to remove trusted player")),

    MEMBERS_STATUS_OFFLINE("&cOffline"),
    MEMBERS_STATUS_ONLINE("&aOnline"),

    DISBAND_TITLE("Disband Settlement?"),
    UN_TRUST_TITLE("Remove Trust for {0}?"),

    // Claims Menu
    CLAIMS_TITLE("&a{0}, {1}"),
    CLAIMS_LORE(List.of("&7{0}", "", "&fClick to teleport")),

    // Roles Menu
    ROLE_DELETE_TITLE("&cDelete"),
    ROLE_DELETE_LORE(List.of("&7Deletes the role from the settlement", "","&fClick to delete role")),
    ROLE_DEFAULT_TITLE("&bDefault"),
    ROLE_DEFAULT_LORE(List.of("&7Makes this role default", "", "&fClick to make default role"));

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