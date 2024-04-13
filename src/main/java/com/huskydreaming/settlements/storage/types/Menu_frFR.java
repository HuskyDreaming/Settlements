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
    ADMIN_DISABLED_WORLD_LORE(List.of("&7Ce monde n'est pas valide.")),
    ADMIN_DISABLED_WORLDS_TITLE("&eMondes désactivés"),
    ADMIN_DISABLED_WORLDS_LORE(List.of("&7{0}", "", "&fCliquez pour éditer les mondes")),

    ADMIN_NOTIFICATION_TITLE("&eNotifications ?"),
    ADMIN_NOTIFICATION_LORE(List.of(
            "&7Notification actuelle : &b{0}",
            "",
            "&fCliquez pour définir le type de notification"
    )),

    CONFIRMATION_YES_TITLE("&a&lOui"),
    CONFIRMATION_NO_TITLE("&c&lNon"),

    // Settlement Menu
    SETTLEMENT_TITLE("&e{0}. {1}"),
    SETTLEMENT_LORE(List.of(
            "",
            "&7Dirigeant : &f{0}",
            "&7Membres : &b{1}&3/&b{2}",
            "&7Terrains : &a{3}&2/&a{4}",
            "&7Rôles : &e{5}&6/&e{6}"
    )),
    SETTLEMENT_MEMBERS_TITLE("&aMembres"),
    SETTLEMENT_MEMBERS_LORE(List.of("&7Affiche tous les membres de la colonie.", "", "&fCliquez pour voir les membres")),
    SETTLEMENT_ROLES_TITLE("&aRôles"),
    SETTLEMENT_ROLES_LORE(List.of("&7Affiche tous les rôles", "", "&fCliquer pour afficher les rôles")),
    SETTLEMENT_ROLE_EDIT_TITLE("&a{0}. {1} &7{2}"),
    SETTLEMENT_ROLE_EDIT_DEFAULT("(Défault)"),
    SETTLEMENT_ROLE_EDIT_LORE(List.of("&7Partie des rôles de votre colonie", "", "&fCliquez pour éditer le rôle")),
    SETTLEMENT_CLAIMS_TITLE("&aTerrains revendiqués"),
    SETTLEMENT_CLAIMS_LORE(List.of("&7Afficher toutes les revendications de terrain", "", "&fCliquez pour éditer les revendications")),
    SETTLEMENT_FLAGS_TITLE("&aDrapeaux"),
    SETTLEMENT_FLAGS_LORE(List.of("&7Cliquez pour ajuster les drapeaux.", "", "&f&oLes drapeaux ne fonctionnent pas.")),
    SETTLEMENT_SPAWN_TITLE("&aPoint d'apparition"),
    SETTLEMENT_SPAWN_LORE(List.of("&7Point d'apparition définis à votre localisation", "", "&fCliquez pour définir le point d'apparition")),
    SETTLEMENT_DISBAND_TITLE("&cDissoudre"),
    SETTLEMENT_DISBAND_LORE(List.of("&7Cela supprimera définitivement la colonie", "", "&fCliquez pour dissourde la colonie")),
    SETTLEMENT_INFO_TITLE("&aInformations de la colonie"),
    SETTLEMENT_INFO_LORE(List.of(
            "&7&O{0}",
            "",
            "&7Dirigeant : &f{1}",
            "",
            "&7Membres : &b{2}&3/&b{3}",
            "&7Terrains : &a{4}&2/&a{5}",
            "&7Rôles : &e{6}&6/&e{7}"
    )),
    //Members Menu
    MEMBER_SET_OWNER_TITLE("&aDéfinir le dirigeant"),
    MEMBER_SET_OWNER_LORE(List.of("&7Le dirigeant actuel est {0}", "", "&fCliquez pour définir le dirigeant")),
    MEMBER_SET_ROLE_TITLE("&aDéfinir le rôle"),
    MEMBER_SET_ROLE_LORE(List.of(
            "&7Rôle actuel : &b{0}. {1}",
            "",
            "&fCliquez gauche > Promotion",
            "&fClique droit > Rétrograder"
    )),
    MEMBER_TELEPORT_TITLE("&aTéléporter"),
    MEMBER_TELEPORT_LORE(List.of("&7Vous téléporte vers la localisation de ce joueur", "","&fCliquez pour téléporter vers une joueur")),
    MEMBER_KICK_TITLE("&aBannir"),
    MEMBER_KICK_LORE(List.of("&7Bannir les membres de la colonie","", "&fCliquez pour bannir un joueur")),
    // Citizens Menu
    MEMBERS_TITLE("&e{0}. {1}"),
    MEMBERS_LORE(List.of(
            "&7Derniers en ligne {2}",
            "",
            "&f{0} &7| {1}",
            "",
            "&fCliquez pour éditer le joueur."
    )),

    TRUSTED_TITLE("&e{0}. {1} &f[Confiance]"),
    TRUSTED_LORE(List.of("&7Un membre de confiance", "", "&fCliquez pour supprimer la confiance du membre.")),

    MEMBERS_STATUS_OFFLINE("&cHors ligne"),
    MEMBERS_STATUS_ONLINE("&aEn ligne"),

    DISBAND_TITLE("Dissoudre la colonie ?"),
    UN_TRUST_TITLE("Supprimer la confiance de ce joueur ?"),

    // Claims Menu
    CLAIMS_TITLE("&a{0}, {1}"),
    CLAIMS_LORE(List.of("&7{0}", "", "&fCliquez pour téléporter")),

    // Roles Menu
    ROLE_DELETE_TITLE("&cSupprimer"),
    ROLE_DELETE_LORE(List.of("&7Supprimer le rôle de cette colonie", "","&fClqiuez pour supprimer le rôle")),
    ROLE_DEFAULT_TITLE("&bDéfault"),
    ROLE_DEFAULT_LORE(List.of("&7Définir ce rôle par défault", "", "&fCliquez pour définir le rôle par défault"));

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