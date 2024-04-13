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
            "&f/admin setowner [settlement] [player] &8| &7D�finir un dirigeant pour la colonie.",
            "&f/admin unclaim &8| &7R�voquer des terrains pour la colonie."
    )),

    // CONFIG - FR
    CONFIG_DISABLED_WORLDS("Mondes qui ne peuvent pas �tre revendiquer par des colonies."),
    CONFIG_PLACEHOLDER_STRING("Remplacement lorsque l'espace r�serv� est nul ou vide"),
    CONFIG_ROLES("R�les par d�fault lors de la fondation d'une colonie."),
    CONFIG_FLAGS("Drapeaux par d�fault lors de la fondation d'une colonie."),
    CONFIG_TRUSTING("Autoriser/Refuser les jours d'ajouter des membres v�rifi�s."),
    CONFIG_TELEPORTATION("Activer/D�sactiver compl�tement la t�l�portation."),
    CONFIG_SETTLEMENT("Valeurs par d�faut lors de la fondation d'un colonie.'"),
    CONFIG_NOTIFICATION("Type de notification lors de l'entr�e/sortie d'un terrain revendiqu�."),

    // Flags
    FLAGS_ANIMAL_SPAWNING("Activer l'apparition d'animaux dans les terrains revendiqu�s."),
    FLAGS_MONSTER_SPAWNING("Activer l'apparition des monstres dans les terrains revendiqu�s."),
    FLAGS_ENTITY_GRIEF("Entities breaking/placing blocks inside claim"),
    FLAGS_LEAF_DECAY("Les feuilles disparaissent lentement dans les terrains revendiqu�s"),
    FLAGS_EXPLOSIONS("Explosions quelconque dans les terrains revendiqu�s"),

    //PERMISSIONS
    PERMISSIONS_CLAIM_BREAK("Les membres peuvent casser des blocs."),
    PERMISSIONS_CLAIM_PLACE("Les membres peuvent placer des blocs."),
    PERMISSIONS_CLAIM_INTERACT("Les membres peuvent int�ragir avec les blocs."),
    PERMISSIONS_CLAIM_LAND("Les membres peuvent revendiquer et abandonner un terrain."),
    PERMISSIONS_EDIT_MEMBERS("Acc�s pour �diter les membres par commandes et interface"),
    PERMISSIONS_EDIT_CLAIMS("Acc�s pour �diter les claims par commandes et interface"),
    PERMISSIONS_EDIT_SPAWN("Acc�s pour �diter les spawn par commandes et interface"),
    PERMISSIONS_EDIT_ROLES("Acc�s pour �diter les r�les par commandes et interface"),
    PERMISSIONS_EDIT_FLAGS("Acc�s pour �diter les flags par interface"),
    PERMISSIONS_EDIT_DESCRIPTION("Acc�s pour �diter les claims par commande"),
    PERMISSIONS_EDIT_TAGS("Acc�s pour �diter les tags par commande"),
    PERMISSIONS_MEMBER_KICK("Bannir un membre de la colonie."),
    PERMISSIONS_MEMBER_TRUST("Ajouter/Supprimer des membres de confiance."),
    PERMISSIONS_MEMBER_INVITE("Inviter un mmebre dans la colonie."),
    PERMISSIONS_MEMBER_KICK_EXEMPT("Immunise un membre d'�tre bannis."),
    PERMISSIONS_MEMBER_FRIENDLY_FIRE("Accepter les d�g�ts entre membres de la colonie."),
    PERMISSIONS_SPAWN_TELEPORT("Accepter la t�l�portation des membres vers le spawn."),

    // Settlements
    SETTLEMENT_AUTO_CLAIM_ON("Maintenant, vous revendiquerez automatiquement les terrains en marchant dans les terres sauvages."),
    SETTLEMENT_AUTO_CLAIM_OFF("Vous ne revendiquerez plus les terrains en marchant."),
    SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND("Limite maximale de revendications atteinte, d�sactivation de la revendication automatique."),
    SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND("Vous ne pouvez pas revendiquer d'avantage de terrains pour votre colonie car la limite est atteinte."),
    SETTLEMENT_CREATE_DISABLED_WORLD("Vous n'�tes pas autoriser a cr�er une colonie dans ce monde."),
    SETTLEMENT_CREATE_WORLDGUARD("Vous ne pouvez pas cr�er une colonie ici car ce terrain est prot�g� par worldguard."),
    SETTLEMENT_CREATE_MIN_NAME_LENGTH("Le nom de la colonie doit contenir au moins &b{0} &7caract�res."),
    SETTLEMENT_CREATE_MAX_NAME_LENGTH("Le nom de la colonie a atteint sa limite de &b{0} &7caract�res."),
    SETTLEMENT_CREATED("Vous avez cr�� une nouvelle colonie au nom de &b{0}&7."),
    SETTLEMENT_DESCRIPTION("Vous venez de d�finir la description de votre colonie par : &b{0}"),
    SETTLEMENT_DESCRIPTION_LONG("La description ne peut exc�der &b{0} &7caract�res."),
    SETTLEMENT_DESCRIPTION_SHORT("La description doit faire au moins &b{0} &7caract�res."),

    SETTLEMENT_TAG("Vous avez ajouter � votre colonie le tag : &b{0}"),
    SETTLEMENT_TAG_LONG("Le tag ne peut exc�der &b{0} &7caract�res."),
    SETTLEMENT_TAG_SHORT("Le tag doit faire au moins &b{0} &7caract�res."),
    SETTLEMENT_DISBAND_YES("Vous avez dissoud la colonie."),
    SETTLEMENT_DISBAND_NO("Dissolution de la colonie annul�e."),
    SETTLEMENT_ESTABLISHED("Une colonie a d�j� �t� �tablis ici."),
    SETTLEMENT_ESTABLISHED_ADJACENT("Vous �tes trop proche d'une colonie pour en cr�er une ici."),
    SETTLEMENT_EXIST("Ce nom de colonie a d�j� �t� pris."),
    SETTLEMENT_JOIN("Vous avez rejoins la &7colonie &b{0}."),
    SETTLEMENT_JOIN_PLAYER("&b{0} &7a rejoins la colonie."),
    SETTLEMENT_KICK("Vous avez �t� bannis de la &7colonie &b{0}."),
    SETTLEMENT_KICK_PLAYER("Vous avez bannis &b{0} &7de la colonie."),
    SETTLEMENT_KICK_EXEMPT("Le joueur ne peut �tre bannis de la colonie."),
    SETTLEMENT_LAND_ADJACENT("Vous devez revendiquer les terres adjacentes � la colonie."),
    SETTLEMENT_LAND_ADJACENT_OTHER("Vous ne pouvez pas revendiquer une terre trop proche d'une autre colonie."),
    SETTLEMENT_LAND_CLAIMED("Ce terrain a d�j� �t� revendiqu�."),
    SETTLEMENT_LAND_CLAIMED_MAX("Votre colonie ne peut revendiquer plus de &b{0}&7 morceau(x) de terrain."),
    SETTLEMENT_LAND_NOT_CLAIMED("Ce terrain n'a pas encore �t� revendiqu�."),
    SETTLEMENT_LAND_CLAIM("Vous avez revendiquer un nouveau terrain &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("Vous avez r�voquer un terrain de la colonie."),
    SETTLEMENT_LAND_UNCLAIM_ONE("The settlement only has only one claimed land. You can't unclaim more land for the settlement."),
    SETTLEMENT_LAND_WORLDGUARD("You can't claim this region as it is protected by worldguard."),
    SETTLEMENT_LAND_TOWNY("You can't claim this region as it is protected by towny."),
    SETTLEMENT_LAND_DISABLED_WORLD("You are not allowed to claim land in this world."),
    SETTLEMENT_LEAVE("Vous avez quitter la colonie."),
    SETTLEMENT_LEAVE_PLAYER("&b{0} &7a quitt� la colonie."),
    SETTLEMENT_LEAVE_OWNER("Vous ne pouvez pas quitter la colonie car vous en �tes le dirigeant. Transf�rez vos pouvoirs avant de pouvoir quitter."),
    SETTLEMENT_NULL("La colonie &b{0} &7ne semble pas exister."),
    SETTLEMENT_NOT_CITIZEN("Le joueur n'est pas un citoyen de votre colonie."),
    SETTLEMENT_OWNER("Vous devenez le nouveau dirigeant de la colonie."),
    SETTLEMENT_NOT_OWNER("Vous devez �tre dirigeant afin d'effectuer cette op�ration."),
    SETTLEMENT_NOT_OWNER_TRANSFER("Vous devez �tre le dirigeant afin de succ�der les pouvoirs."),
    SETTLEMENT_IS_OWNER("Vous �tes d�j� dirigeant de la colonie."),
    SETTLEMENT_IS_MEMBER("Le joueur &b{0} &7fait d�j� partie de votre colonie."),
    SETTLEMENT_IS_TRUSTED("Le joueur &b{0} &7est d�j� de confiance."),
    SETTLEMENT_IS_NOT_TRUSTED("Le joueur &b{0} &7n'est pas de confiance dans votre colonie."),
    SETTLEMENT_OWNER_TRANSFERRED("Vous avez transf�r� vos pouvoirs de dirigeant � &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("Vous faites d�j� partie d'une colonie."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("Le joueur &b{0} &7est d�j� membre d'une colonie."),
    SETTLEMENT_PLAYER_NULL("Vous ne semblez pas appartenir � une colonie."),
    SETTLEMENT_ROLE_CREATE("Le &7r�le &b{0} a �t� cr�� avec succ�s."),
    SETTLEMENT_ROLE_DELETE("Le &7r�le &b{0} a �t� supprim� avec succ�s."),
    SETTLEMENT_ROLE_EXISTS("La colonie a d�j� le &7r�le suivant &b{0}."),
    SETTLEMENT_ROLE_NULL("Le r�le que vous souhaitez supprimer n'�xiste pas."),
    SETTLEMENT_ROLE_ONE("La colonie doit avoir au moins un r�le."),
    SETTLEMENT_SET_SPAWN("Vous avez d�finis le point d'apparition de votre colonie � votre position actuelle."),
    SETTLEMENT_SPAWN("Vous avez �t� t�l�port� au point d'apparation de la colonie."),
    SETTLEMENT_SPAWN_NULL("Le point d'apparition pour la colonie a �t� d�finis."),
    SETTLEMENT_TELEPORT("Vous avez �t� t�l�port� vers &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_TRUST("Le joueur &b{0} &7 est de confiance."),
    SETTLEMENT_TRUST_OFFLINE_PLAYER("Vous �tes d�sormais de confiance dans la &7colonie &b{0}."),
    SETTLEMENT_TRUST_SELF("Vous ne pouvez pas vous faire confiance vous-m�me?!?"),
    SETTLEMENT_TRUST_REMOVE("Le joueur &b{0} &7n'est plus de confiance."),
    SETTLEMENT_TRUST_REMOVE_OFFLINE_PLAYER("Le joueur &b{0} &7n'est plus de confiance."),
    SETTLEMENT_LIST_NULL("Aucune colonie n'a �t� fond�e."),

    // Notifications
    NOTIFICATION_TITLE_HEADER("{0}&l{1}"),
    NOTIFICATION_TITLE_FOOTER("&7{0}"),
    NOTIFICATION_BOSS_BAR("&7Entr�e {0}&l{1}"),
    NOTIFICATION_ACTION_BAR("&Entr�e {0}&l{1}"),
    NOTIFICATION_WILDERNESS_TITLE_HEADER("&a&lTerres sauvages"),
    NOTIFICATION_WILDERNESS_TITLE_FOOTER("&7Un nouveau monde vous attends"),
    NOTIFICATION_WILDERNESS_BOSS_BAR("&7Entr�e dans les &a&lTerres sauvages"),
    NOTIFICATION_WILDERNESS_ACTION_BAR("&7Entr�e dans les &a&lTerres sauvages"),

    // Helps pages
    HELP_PAGE_HEADER("&e&lAide de la colonie : &a{0}&2/&a{1}"),
    HELP_PAGE_FORMAT("  &b{0}. &f{1}"),
    HELP_PAGE_LIMIT("Il n'y a que &b{0} &7pages disponibles."),
    HELP_PAGE_NEXT(">>"),
    HELP_PAGE_PREVIOUS("<<"),
    HELP_PAGE_DISABLED("&7"),
    HELP_PAGE_ENABLED("&e"),

    // Invitations
    INVITATION_DENIED("Vous avez refus� l'invitation de la colonie &b{0}&7."),
    INVITATION_SELF("Vous ne pouvez pas vous invitez vous-m�me."),
    INVITATION_NULL("Vous n'avez pas d'invitation pour '&b{0}&7."),
    INVITATION_RECEIVED("Vous avez �t� invit� � rejoinre &b{0}&7. Souhaitez-vous accepter ?"),
    INVITATION_SENT("Vous avez inviter &b{0}&7."),
    INVITATION_ACCEPT("&a[Accepter]"),
    INVITATION_DENY("&c[Refuser]"),
    NO_PERMISSIONS("Vous n'avez pas la permission."),
    RELOAD("Configuration recharg�e avec succ�s."),
    UNKNOWN_SUBCOMMAND("&b{0} &7n'est pas une sous-commande valide."),
    PLAYER_NULL("Le joueur &b{0} &7n'a jamais jouer avant'."),
    PLAYER_OFFLINE("Le joueur &b{0} &7ne semble pas �tre en ligne."),
    PLAYER_TELEPORT("Vous avez �t� t�l�port� : &b{0}&7.");

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