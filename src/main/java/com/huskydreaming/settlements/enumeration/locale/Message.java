package com.huskydreaming.settlements.enumeration.locale;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Message implements Parseable {

    // GENERAL
    GENERAL_PREFIX("&aSettlements: &7"),
    GENERAL_TELEPORT("You have teleported to &7x: &b{0}&7, z: &b{1}"),

    GENERAL_NO_PERMISSIONS("You do not have permissions."),
    GENERAL_NULL( "No settlements have been created."),
    GENERAL_RELOAD("You have successfully reloaded all the configurations."),
    GENERAL_UNKNOWN_SUBCOMMAND("&b{0} &7is not a valid subcommand."),

    GENERAL_ADMIN_HELP(List.of(
            "&cAdmin Help:",
            "",
            "&f/admin claim [settlement] &8| &7Claims land for a settlement",
            "&f/admin disband [settlement] &8| &7Disbands a settlement",
            "&f/admin help &8| &7Displays admin help pages",
            "&f/admin setdescription [settlement] [description] &8| &7Sets a description for settlement",
            "&f/admin setowner [settlement] [player] &8| &7Sets an owner for a settlement",
            "&f/admin unclaim &8| &7Unclaims land for a settlement"
    )),

    // Auto claim
    AUTO_CLAIM_OFF("You will no longer claim land automatically."),
    AUTO_CLAIM_OFF_MAX_LAND("You are not able to claim any more land, turning off auto claim."),
    AUTO_CLAIM_ON("You will now claim land automatically when walking into the wilderness."),
    AUTO_CLAIM_ON_MAX_LAND("You are not able to automatically claim new land as your settlement has reached the maximum number of claims."),

    // CONFIG
    CONFIG_DISABLED_WORLDS("Worlds that will not be affected by settlements"),
    CONFIG_FLAGS("Default flags when a settlement is made"),
    CONFIG_NOTIFICATION("Type of notification entering/exiting claim"),
    CONFIG_PLACEHOLDER_STRING("Replacement when placeholder is null or empty"),
    CONFIG_ROLES("Default roles when a settlement is made"),
    CONFIG_SETTLEMENT("Default values for when a settlement is made"),
    CONFIG_TELEPORTATION("Enable/disable teleportation completely"),
    CONFIG_TRUSTING("Allow/Disallow players to add trusted members"),

    // Citizen
    NOT_CITIZEN("The player is not a citizen of your settlement."),

    // Create
    CREATE_DISABLED_WORLD("You are not able to create a settlement in this world."),
    CREATE_EXISTS("A settlement with that name already exists."),
    CREATE_MAX_NAME_LENGTH("The settlement name can't be longer than &b{0} &7characters."),
    CREATE_MIN_NAME_LENGTH("The name of the settlement must be at least &b{0} &7characters."),
    CREATE_SETTLEMENT("You have created a new settlement named &b{0}&7."),
    CREATE_WORLD_GUARD("You are not able to create a settlement here as it is protected by worldguard."),

    // Description
    DESCRIPTION("You have set the settlement description to: &b{0}"),
    DESCRIPTION_LONG("The description can't be greater than &b{0} &7characters."),
    DESCRIPTION_SHORT("The description has to be greater than &b{0} &7characters."),

    // Disband
    DISBAND_NO("You decided not to disband the settlement"),
    DISBAND_YES("You have disbanded the settlement."),

    // Flags
    FLAGS_ANIMAL_KILLING("Allows animals to be killed inside the claim"),
    FLAGS_ANIMAL_SPAWNING("Allow animals to spawn inside claim"),
    FLAGS_ENTITY_GRIEF("Entities breaking/placing blocks inside claim"),
    FLAGS_EXPLOSIONS("Explosions by mobs or other reasons inside claim"),
    FLAGS_LEAF_DECAY("Leafs slowly decaying inside claim"),
    FLAGS_MONSTER_SPAWNING("Allow monsters to spawn inside claim"),

    // Notifications
    NOTIFICATION_ACTION_BAR("&7Entered {0}&l{1}"),
    NOTIFICATION_BOSS_BAR("&7Entered {0}&l{1}"),
    NOTIFICATION_TITLE_FOOTER("&7{0}"),
    NOTIFICATION_TITLE_HEADER("{0}&l{1}"),
    NOTIFICATION_WILDERNESS_ACTION_BAR("&7Entered the &a&lWilderness"),
    NOTIFICATION_WILDERNESS_BOSS_BAR("&7Entered the &a&lWilderness"),
    NOTIFICATION_WILDERNESS_TITLE_FOOTER("&7Fresh new land awaits you"),
    NOTIFICATION_WILDERNESS_TITLE_HEADER("&a&lWilderness"),

    // Null
    NULL("The settlement &b{0} &7does not seem to exist."),

    // Helps pages
    HELP_PAGE_DISABLED("&7"),
    HELP_PAGE_ENABLED("&e"),
    HELP_PAGE_FORMAT("  &b{0}. &f{1}"),
    HELP_PAGE_HEADER("&e&lSettlement Help: &a{0}&2/&a{1}"),
    HELP_PAGE_LIMIT("There are only &b{0} &7pages available."),
    HELP_PAGE_NEXT(">>"),
    HELP_PAGE_PREVIOUS("<<"),

    // HOMES
    HOME_DELETE("You have deleted &b{0} &7home."),
    HOME_EXISTS("The &b{0} &7home already exists."),
    HOME_MAX("Your settlement can't have more than &b{0}&7 home(s)."),
    HOME_NULL("The home &b{0} &7does not seem to exist."),
    HOME_SET("You have created &b{0} &7home."),
    HOME_TELEPORT("You have been teleported to &b{0}&7."),

    // Invitations
    INVITATION_ACCEPT("&a[Accept]"),
    INVITATION_DENIED("You have denied an invitation for &b{0}&7."),
    INVITATION_DENY("&c[Deny]"),
    INVITATION_NULL("You do not have an invitation for &b{0}&7."),
    INVITATION_RECEIVED("You have been invited to join &b{0}&7. Do you wish to accept?"),
    INVITATION_SELF("You can't invite yourself."),
    INVITATION_SENT("You have sent an invitation to &b{0}&7."),

    // Join
    JOIN("You have joined the &b{0} &7settlement."),
    JOIN_PLAYER("&b{0} &7has joined the settlement."),

    // Kick
    KICK("You have been kicked from the &b{0} &7settlement."),
    KICK_EXEMPT("This player can't be kicked from the settlement."),
    KICK_PLAYER("You have kicked &b{0} &7from the settlement."),

    // Land
    LAND_ADJACENT("You must claim land that is adjacent to the settlement."),
    LAND_ADJACENT_OTHER("You can't claim land that is too close to another settlement."),
    LAND_CLAIM("You have claimed new land &7x: &b{0}&7, z: &b{1}"),
    LAND_CLAIMED("This land has already been claimed."),
    LAND_CLAIMED_MAX("Your settlement can't claim more than &b{0}&7 piece(s) of land."),
    LAND_DISABLED_WORLD("You are not allowed to claim land in this world."),
    LAND_ESTABLISHED("A settlement has already been established here."),
    LAND_ESTABLISHED_ADJACENT("You are too close to another settlement to create one here."),
    LAND_NOT_CLAIMED("This land has not been claimed."),
    LAND_TOWNY("You can't claim this region as it is protected by towny."),
    LAND_UN_CLAIM("You have unclaimed the land from the settlement."),
    LAND_UN_CLAIM_ONE("The settlement only has only one claimed land. You can't unclaim more land for the settlement."),
    LAND_WORLD_GUARD("You can't claim this region as it is protected by worldguard."),

    // Leave
    LEAVE("You have left the settlement."),
    LEAVE_OWNER("You are not able to leave the settlement because you are the owner. You must transfer ownership first."),
    LEAVE_PLAYER("&b{0} &7has left the settlement."),

    // Member
    MEMBER_ALREADY("The player &b{0} &7is already part of your settlement."),
    MEMBER_MAX("Your settlement can't have more than &b{0}&7 member(s)."),

    // Owner
    OWNER("You have become the owner of your settlement."),
    OWNER_CURRENT("You are already the owner of the settlement."),
    OWNER_NOT("You must be owner to proceed with this action."),
    OWNER_TRANSFER_FALSE("You must be the settlement owner to transfer ownership."),
    OWNER_TRANSFERRED("You have transferred ownership of your settlement to &f{0}&7."),

    // Permissions
    PERMISSIONS_CLAIM_BREAK("Members can break blocks"),
    PERMISSIONS_CLAIM_INTERACT("Members can interact with blocks"),
    PERMISSIONS_CLAIM_LAND("Members can claim and un-claim land"),
    PERMISSIONS_CLAIM_PLACE("Members can place blocks"),
    PERMISSIONS_CLAIM_TELEPORT("Members can teleport to claim coordinates"),
    PERMISSIONS_EDIT_CLAIMS("Access to edit claims via commands and ui"),
    PERMISSIONS_EDIT_DESCRIPTION("Access to edit claims via a command"),
    PERMISSIONS_EDIT_FLAGS("Access to edit flags via ui"),
    PERMISSIONS_EDIT_HOMES("Access to edit homes via commands and ui"),
    PERMISSIONS_EDIT_MEMBERS("Access to edit members via commands and ui"),
    PERMISSIONS_EDIT_SPAWN("Access to edit spawn via commands and ui"),
    PERMISSIONS_EDIT_ROLES("Access to edit roles via commands and ui"),
    PERMISSIONS_EDIT_TAGS("Access to edit tags via a commands"),
    PERMISSIONS_HOME_TELEPORT("Allows members to teleport to a home"),
    PERMISSIONS_MEMBER_FRIENDLY_FIRE("Allows members to damage other members"),
    PERMISSIONS_MEMBER_INVITE("Invite members to the settlements"),
    PERMISSIONS_MEMBER_KICK("Kick members from the settlement"),
    PERMISSIONS_MEMBER_KICK_EXEMPT("Exempts members from being kicked"),
    PERMISSIONS_MEMBER_TRUST("Add/Remove trusted members"),
    PERMISSIONS_SPAWN_TELEPORT("Allows members to teleport to spawn"),

    // Player
    PLAYER_EXISTS("You are already part of a settlement."),
    PLAYER_HAS_SETTLEMENT("The player &b{0} &7already has a settlement."),
    PLAYER_NEVER_PLAYED("The player &b{0} &7has never played before."),
    PLAYER_NULL("You do not seem to belong to a settlement."),
    PLAYER_OFFLINE("The player &b{0} &7does not seem to be online."),
    PLAYER_TELEPORT("You have been teleported to &b{0}&7."),

    // ROLES
    ROLE_CREATE("Successfully created the &b{0} &7role."),
    ROLE_DEFAULT("You are not able to join &b{0}&7 because they do not have a default role setup."),
    ROLE_DELETE("Successfully deleted the &b{0} &7role."),
    ROLE_EXISTS("The settlement already has the &b{0} &7role."),
    ROLE_MAX("Your settlement can't have more than &b{0}&7 role(s)."),
    ROLE_NULL("The &b{0} &7role does not seem to exist."),
    ROLE_ONE("The settlement must have at least one role available."),
    ROLE_SET("You have set the role of &b{0} &7to &b{1}&7."),
    ROLE_SET_OTHER("You have been assigned the &b{0}&7 role."),

    // Spawn
    SPAWN_NULL("The settlement spawn does not seem to exist."),
    SPAWN_SET("You have set the spawn for the settlement at your location."),
    SPAWN_TELEPORT("You have been teleported to the settlement spawn."),

    // Tags
    TAG_LONG("The tag can't be greater than &b{0} &7characters."),
    TAG_SET("You have set the settlement tag to: &b{0}"),
    TAG_SHORT("The tag has to be greater than &b{0} &7characters."),

    // Trusting
    TRUST("The player &b{0} &7has been trusted."),
    TRUSTED_ALREADY("The player &b{0} &7is already trusted."),
    TRUSTED_NOT("The player &b{0} &7is not trusted to your settlement."),
    TRUST_OFFLINE_PLAYER("You have been trusted to the &b{0} &7settlement."),
    TRUST_REMOVE("The player &b{0} &7is not longer trusted."),
    TRUST_REMOVE_OFFLINE_PLAYER("The player &b{0} &7is not longer trusted."),
    TRUST_SELF("You can't trust yourself?!?");

    private final String def;
    private final List<String> list;
    private static FileConfiguration localeConfiguration;

    Message(String def) {
        this.def = def;
        this.list = null;
    }

    Message(List<String> list) {
        this.list = list;
        this.def = null;
    }

    public String prefix(Object... objects) {
        return Message.GENERAL_PREFIX.parse() + parameterize(objects);
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
        Message.localeConfiguration = configuration;
    }
}