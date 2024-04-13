package com.huskydreaming.settlements.storage.types;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Locale implements Parseable {
    PREFIX("&Colonies : &7"),
    ADMIN_HELP(List.of(
            "&cAide Admin :",
            "",
            "&f/admin claim [settlement] &8| &7Revendiquer un terrain pour la colonie.",
            "&f/admin disband [settlement] &8| &7Dissourde une colonie.",
            "&f/admin help &8| &7Activer la page d'aide des admins.",
            "&f/admin setdescription [settlement] [description] &8| &7Ajouter une description pour la colonie.",
            "&f/admin setowner [settlement] [player] &8| &7Définir un dirigeant pour la colonie.",
            "&f/admin unclaim &8| &7Révoquer des terrains pour la colonie."
    )),

    // CONFIG - FR
    CONFIG_DISABLED_WORLDS("Mondes qui ne peuvent pas être revendiquer par des colonies."),
    CONFIG_PLACEHOLDER_STRING("Remplacement lorsque l'espace réservé est nul ou vide"),
    CONFIG_ROLES("Rôles par défault lors de la fondation d'une colonie."),
    CONFIG_FLAGS("Drapeaux par défault lors de la fondation d'une colonie."),
    CONFIG_TRUSTING("Autoriser/Refuser les jours d'ajouter des membres vérifiés."),
    CONFIG_TELEPORTATION("Activer/Désactiver complètement la téléportation."),
    CONFIG_SETTLEMENT("Valeurs par défaut lors de la fondation d'un colonie.'"),
    CONFIG_NOTIFICATION("Type de notification lors de l'entrée/sortie d'un terrain revendiqué."),

    // Flags
    FLAGS_ANIMAL_SPAWNING("Activer l'apparition d'animaux dans les terrains revendiqués."),
    FLAGS_MONSTER_SPAWNING("Activer l'apparition des monstres dans les terrains revendiqués."),
    FLAGS_ENTITY_GRIEF("Entities breaking/placing blocks inside claim"),
    FLAGS_LEAF_DECAY("Les feuilles disparaissent lentement dans les terrains revendiqués"),
    FLAGS_EXPLOSIONS("Explosions quelconque dans les terrains revendiqués"),

    //PERMISSIONS
    PERMISSIONS_CLAIM_BREAK("Les membres peuvent casser des blocs."),
    PERMISSIONS_CLAIM_PLACE("Les membres peuvent placer des blocs."),
    PERMISSIONS_CLAIM_INTERACT("Les membres peuvent intéragir avec les blocs."),
    PERMISSIONS_CLAIM_LAND("Les membres peuvent revendiquer et abandonner un terrain."),
    PERMISSIONS_EDIT_MEMBERS("Accès pour éditer les membres par commandes et interface"),
    PERMISSIONS_EDIT_CLAIMS("Accès pour éditer les claims par commandes et interface"),
    PERMISSIONS_EDIT_SPAWN("Accès pour éditer les spawn par commandes et interface"),
    PERMISSIONS_EDIT_ROLES("Accès pour éditer les rôles par commandes et interface"),
    PERMISSIONS_EDIT_FLAGS("Accès pour éditer les flags par interface"),
    PERMISSIONS_EDIT_DESCRIPTION("Accès pour éditer les claims par commande"),
    PERMISSIONS_EDIT_TAGS("Accès pour éditer les tags par commande"),
    PERMISSIONS_MEMBER_KICK("Bannir un membre de la colonie."),
    PERMISSIONS_MEMBER_TRUST("Ajouter/Supprimer des membres de confiance."),
    PERMISSIONS_MEMBER_INVITE("Inviter un mmebre dans la colonie."),
    PERMISSIONS_MEMBER_KICK_EXEMPT("Immunise un membre d'être bannis."),
    PERMISSIONS_MEMBER_FRIENDLY_FIRE("Accepter les dégâts entre membres de la colonie."),
    PERMISSIONS_SPAWN_TELEPORT("Accepter la téléportation des membres vers le spawn."),

    // Settlements
    SETTLEMENT_AUTO_CLAIM_ON("Maintenant, vous revendiquerez automatiquement les terrains en marchant dans les terres sauvages."),
    SETTLEMENT_AUTO_CLAIM_OFF("Vous ne revendiquerez plus les terrains en marchant."),
    SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND("Limite maximale de revendications atteinte, désactivation de la revendication automatique."),
    SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND("Vous ne pouvez pas revendiquer d'avantage de terrains pour votre colonie car la limite est atteinte."),
    SETTLEMENT_CREATE_DISABLED_WORLD("Vous n'êtes pas autoriser a créer une colonie dans ce monde."),
    SETTLEMENT_CREATE_WORLDGUARD("Vous ne pouvez pas créer une colonie ici car ce terrain est protégé par worldguard."),
    SETTLEMENT_CREATE_MIN_NAME_LENGTH("Le nom de la colonie doit contenir au moins &b{0} &7caractères."),
    SETTLEMENT_CREATE_MAX_NAME_LENGTH("Le nom de la colonie a atteint sa limite de &b{0} &7caractères."),
    SETTLEMENT_CREATED("Vous avez créé une nouvelle colonie au nom de &b{0}&7."),
    SETTLEMENT_DESCRIPTION("Vous venez de définir la description de votre colonie par : &b{0}"),
    SETTLEMENT_DESCRIPTION_LONG("La description ne peut excéder &b{0} &7caractères."),
    SETTLEMENT_DESCRIPTION_SHORT("La description doit faire au moins &b{0} &7caractères."),

    SETTLEMENT_TAG("Vous avez ajouter à votre colonie le tag : &b{0}"),
    SETTLEMENT_TAG_LONG("Le tag ne peut excéder &b{0} &7caractères."),
    SETTLEMENT_TAG_SHORT("Le tag doit faire au moins &b{0} &7caractères."),
    SETTLEMENT_DISBAND_YES("Vous avez dissoud la colonie."),
    SETTLEMENT_DISBAND_NO("Dissolution de la colonie annulée."),
    SETTLEMENT_ESTABLISHED("Une colonie a déjà été établis ici."),
    SETTLEMENT_ESTABLISHED_ADJACENT("Vous êtes trop proche d'une colonie pour en créer une ici."),
    SETTLEMENT_EXIST("Ce nom de colonie a déjà été pris."),
    SETTLEMENT_JOIN("Vous avez rejoins la &7colonie &b{0}."),
    SETTLEMENT_JOIN_PLAYER("&b{0} &7a rejoins la colonie."),
    SETTLEMENT_KICK("Vous avez été bannis de la &7colonie &b{0}."),
    SETTLEMENT_KICK_PLAYER("Vous avez bannis &b{0} &7de la colonie."),
    SETTLEMENT_KICK_EXEMPT("Le joueur ne peut être bannis de la colonie."),
    SETTLEMENT_LAND_ADJACENT("Vous devez revendiquer les terres adjacentes à la colonie."),
    SETTLEMENT_LAND_ADJACENT_OTHER("Vous ne pouvez pas revendiquer une terre trop proche d'une autre colonie."),
    SETTLEMENT_LAND_CLAIMED("Ce terrain a déjà été revendiqué."),
    SETTLEMENT_LAND_CLAIMED_MAX("Votre colonie ne peut revendiquer plus de &b{0}&7 morceau(x) de terrain."),
    SETTLEMENT_LAND_NOT_CLAIMED("Ce terrain n'a pas encore été revendiqué."),
    SETTLEMENT_LAND_CLAIM("Vous avez revendiquer un nouveau terrain &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("Vous avez révoquer un terrain de la colonie."),
    SETTLEMENT_LAND_UNCLAIM_ONE("The settlement only has only one claimed land. You can't unclaim more land for the settlement."),
    SETTLEMENT_LAND_WORLDGUARD("You can't claim this region as it is protected by worldguard."),
    SETTLEMENT_LAND_TOWNY("You can't claim this region as it is protected by towny."),
    SETTLEMENT_LAND_DISABLED_WORLD("You are not allowed to claim land in this world."),
    SETTLEMENT_LEAVE("Vous avez quitter la colonie."),
    SETTLEMENT_LEAVE_PLAYER("&b{0} &7a quitté la colonie."),
    SETTLEMENT_LEAVE_OWNER("Vous ne pouvez pas quitter la colonie car vous en êtes le dirigeant. Transférez vos pouvoirs avant de pouvoir quitter."),
    SETTLEMENT_NULL("La colonie &b{0} &7ne semble pas exister."),
    SETTLEMENT_NOT_CITIZEN("Le joueur n'est pas un citoyen de votre colonie."),
    SETTLEMENT_OWNER("Vous devenez le nouveau dirigeant de la colonie."),
    SETTLEMENT_NOT_OWNER("Vous devez être dirigeant afin d'effectuer cette opération."),
    SETTLEMENT_NOT_OWNER_TRANSFER("Vous devez être le dirigeant afin de succéder les pouvoirs."),
    SETTLEMENT_IS_OWNER("Vous êtes déjà dirigeant de la colonie."),
    SETTLEMENT_IS_MEMBER("Le joueur &b{0} &7fait déjà partie de votre colonie."),
    SETTLEMENT_IS_TRUSTED("Le joueur &b{0} &7est déjà de confiance."),
    SETTLEMENT_IS_NOT_TRUSTED("Le joueur &b{0} &7n'est pas de confiance dans votre colonie."),
    SETTLEMENT_OWNER_TRANSFERRED("Vous avez transféré vos pouvoirs de dirigeant à &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("Vous faites déjà partie d'une colonie."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("Le joueur &b{0} &7est déjà membre d'une colonie."),
    SETTLEMENT_PLAYER_NULL("Vous ne semblez pas appartenir à une colonie."),
    SETTLEMENT_ROLE_CREATE("Le &7rôle &b{0} a été créé avec succès."),
    SETTLEMENT_ROLE_DELETE("Le &7rôle &b{0} a été supprimé avec succès."),
    SETTLEMENT_ROLE_EXISTS("La colonie a déjà le &7rôle suivant &b{0}."),
    SETTLEMENT_ROLE_NULL("Le rôle que vous souhaitez supprimer n'éxiste pas."),
    SETTLEMENT_ROLE_ONE("La colonie doit avoir au moins un rôle."),
    SETTLEMENT_SET_SPAWN("Vous avez définis le point d'apparition de votre colonie à votre position actuelle."),
    SETTLEMENT_SPAWN("Vous avez été téléporté au point d'apparation de la colonie."),
    SETTLEMENT_SPAWN_NULL("Le point d'apparition pour la colonie a été définis."),
    SETTLEMENT_TELEPORT("Vous avez été téléporté vers &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_TRUST("Le joueur &b{0} &7 est de confiance."),
    SETTLEMENT_TRUST_OFFLINE_PLAYER("Vous êtes désormais de confiance dans la &7colonie &b{0}."),
    SETTLEMENT_TRUST_SELF("Vous ne pouvez pas vous faire confiance vous-même?!?"),
    SETTLEMENT_TRUST_REMOVE("Le joueur &b{0} &7n'est plus de confiance."),
    SETTLEMENT_TRUST_REMOVE_OFFLINE_PLAYER("Le joueur &b{0} &7n'est plus de confiance."),
    SETTLEMENT_LIST_NULL("Aucune colonie n'a été fondée."),

    // Notifications
    NOTIFICATION_TITLE_HEADER("{0}&l{1}"),
    NOTIFICATION_TITLE_FOOTER("&7{0}"),
    NOTIFICATION_BOSS_BAR("&7Entrée {0}&l{1}"),
    NOTIFICATION_ACTION_BAR("&Entrée {0}&l{1}"),
    NOTIFICATION_WILDERNESS_TITLE_HEADER("&a&lTerres sauvages"),
    NOTIFICATION_WILDERNESS_TITLE_FOOTER("&7Un nouveau monde vous attends"),
    NOTIFICATION_WILDERNESS_BOSS_BAR("&7Entrée dans les &a&lTerres sauvages"),
    NOTIFICATION_WILDERNESS_ACTION_BAR("&7Entrée dans les &a&lTerres sauvages"),

    // Helps pages
    HELP_PAGE_HEADER("&e&lAide de la colonie : &a{0}&2/&a{1}"),
    HELP_PAGE_FORMAT("  &b{0}. &f{1}"),
    HELP_PAGE_LIMIT("Il n'y a que &b{0} &7pages disponibles."),
    HELP_PAGE_NEXT(">>"),
    HELP_PAGE_PREVIOUS("<<"),
    HELP_PAGE_DISABLED("&7"),
    HELP_PAGE_ENABLED("&e"),

    // Invitations
    INVITATION_DENIED("Vous avez refusé l'invitation de la colonie &b{0}&7."),
    INVITATION_SELF("Vous ne pouvez pas vous invitez vous-même."),
    INVITATION_NULL("Vous n'avez pas d'invitation pour '&b{0}&7."),
    INVITATION_RECEIVED("Vous avez été invité à rejoinre &b{0}&7. Souhaitez-vous accepter ?"),
    INVITATION_SENT("Vous avez inviter &b{0}&7."),
    INVITATION_ACCEPT("&a[Accepter]"),
    INVITATION_DENY("&c[Refuser]"),
    NO_PERMISSIONS("Vous n'avez pas la permission."),
    RELOAD("Configuration rechargée avec succès."),
    UNKNOWN_SUBCOMMAND("&b{0} &7n'est pas une sous-commande valide."),
    PLAYER_NULL("Le joueur &b{0} &7n'a jamais jouer avant'."),
    PLAYER_OFFLINE("Le joueur &b{0} &7ne semble pas être en ligne."),
    PLAYER_TELEPORT("Vous avez été téléporté : &b{0}&7.");

    private final String def;
    private final List<String> list;
    private static FileConfiguration localeConfiguration;

    Locale(String def) {
        this.def = def;
        this.list = null;
    }

    Locale(List<String> list) {
        this.list = list;
        this.def = null;
    }

    public String prefix(Object... objects) {
        return Locale.PREFIX.parse() + parameterize(objects);
    }

    @Nullable
    public String parse() {
        String message = localeConfiguration.getString(toString(), def);
        if (message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = localeConfiguration.getList(toString(), list);
        if (objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}