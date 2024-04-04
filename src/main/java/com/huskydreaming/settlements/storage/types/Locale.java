package com.huskydreaming.settlements.storage.types;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Locale implements Parseable {
    PREFIX("&aSettlements: &7"),
    ADMIN_HELP(List.of(
            "&cAdmin Help:",
            "",
            "&f/admin claim [settlement] &8| &7Claims land for a settlement",
            "&f/admin disband [settlement] &8| &7Disbands a settlement",
            "&f/admin help &8| &7Displays admin help pages",
            "&f/admin setdescription [settlement] [description] &8| &7Sets a description for settlement",
            "&f/admin setowner [settlement] [player] &8| &7Sets an owner for a settlement",
            "&f/admin unclaim &8| &7Unclaims land for a settlement"
    )),

    // CONFIG
    CONFIG_DISABLED_WORLDS("Worlds that will not be affected by settlements"),
    CONFIG_PLACEHOLDER_STRING("Replacement when placeholder is null or empty"),
    CONFIG_ROLES("Default roles when a settlement is made"),
    CONFIG_FLAGS("Default flags when a settlement is made"),
    CONFIG_TRUSTING("Allow/Disallow players to add trusted members"),
    CONFIG_TELEPORTATION("Enable/disable teleportation completely"),
    CONFIG_SETTLEMENT("Default values for when a settlement is made"),
    CONFIG_NOTIFICATION("Type of notification entering/exiting claim"),

    // Flags
    FLAGS_ANIMAL_SPAWNING("Allow animals to spawn inside claim"),
    FLAGS_MONSTER_SPAWNING("Allow monsters to spawn inside claim"),
    FLAGS_ENTITY_GRIEF("Entities breaking/placing blocks inside claim"),
    FLAGS_LEAF_DECAY("Leafs slowly decaying inside claim"),
    FLAGS_EXPLOSIONS("Explosions by mobs or other reasons inside claim"),

    //PERMISSIONS
    PERMISSIONS_CLAIM_BREAK("Members can break blocks"),
    PERMISSIONS_CLAIM_PLACE("Members can place blocks"),
    PERMISSIONS_CLAIM_INTERACT("Members can interact with blocks"),
    PERMISSIONS_CLAIM_LAND("Members can claim and un-claim land"),
    PERMISSIONS_EDIT_MEMBERS("Access to edit members via commands and ui"),
    PERMISSIONS_EDIT_CLAIMS("Access to edit claims via commands and ui"),
    PERMISSIONS_EDIT_SPAWN("Access to edit spawn via commands and ui"),
    PERMISSIONS_EDIT_ROLES("Access to edit roles via commands and ui"),
    PERMISSIONS_EDIT_FLAGS("Access to edit flags via ui"),
    PERMISSIONS_EDIT_DESCRIPTION("Access to edit claims via a command"),
    PERMISSIONS_EDIT_TAGS("Access to edit tags via a commands"),
    PERMISSIONS_MEMBER_KICK("Kick members from the settlement"),
    PERMISSIONS_MEMBER_TRUST("Add/Remove trusted members"),
    PERMISSIONS_MEMBER_INVITE("Invite members to the settlements"),
    PERMISSIONS_MEMBER_KICK_EXEMPT("Exempts members from being kicked"),
    PERMISSIONS_MEMBER_FRIENDLY_FIRE("Allows members to damage other members"),
    PERMISSIONS_SPAWN_TELEPORT("Allows members to teleport to spawn"),

    // Settlements
    SETTLEMENT_AUTO_CLAIM_ON("You will now claim land automatically when walking into the wilderness."),
    SETTLEMENT_AUTO_CLAIM_OFF("You will no longer claim land automatically."),
    SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND("You are not able to claim any more land, turning off auto claim."),
    SETTLEMENT_AUTO_CLAIM_ON_MAX_LAND("You are not able to automatically claim new land as your settlement has reached the maximum number of claims."),
    SETTLEMENT_CREATE_DISABLED_WORLD("You are not able to create a settlement in this world."),
    SETTLEMENT_CREATE_WORLDGUARD("You are not able to create a settlement here as it is protected by worldguard."),
    SETTLEMENT_CREATE_MIN_NAME_LENGTH("The name of the settlement must be at least &b{0} &7characters."),
    SETTLEMENT_CREATE_MAX_NAME_LENGTH("The settlement name can't be longer than &b{0} &7characters."),
    SETTLEMENT_CREATED("You have created a new settlement named &b{0}&7."),
    SETTLEMENT_DESCRIPTION("You have set the settlement description to: &b{0}"),
    SETTLEMENT_DESCRIPTION_LONG("The description can't be greater than &b{0} &7characters."),
    SETTLEMENT_DESCRIPTION_SHORT("The description has to be greater than &b{0} &7characters."),

    SETTLEMENT_TAG("You have set the settlement tag to: &b{0}"),
    SETTLEMENT_TAG_LONG("The tag can't be greater than &b{0} &7characters."),
    SETTLEMENT_TAG_SHORT("The tag has to be greater than &b{0} &7characters."),
    SETTLEMENT_DISBAND_YES("You have disbanded the settlement."),
    SETTLEMENT_DISBAND_NO("You decided not to disband the settlement"),
    SETTLEMENT_ESTABLISHED("A settlement has already been established here."),
    SETTLEMENT_ESTABLISHED_ADJACENT("You are too close to another settlement to create one here."),
    SETTLEMENT_EXIST("A settlement with that name already exists."),
    SETTLEMENT_JOIN("You have joined the &b{0} &7settlement."),
    SETTLEMENT_JOIN_PLAYER("&b{0} &7has joined the settlement."),
    SETTLEMENT_KICK("You have been kicked from the &b{0} &7settlement."),
    SETTLEMENT_KICK_PLAYER("You have kicked &b{0} &7from the settlement."),
    SETTLEMENT_KICK_EXEMPT("This player can't be kicked from the settlement."),
    SETTLEMENT_LAND_ADJACENT("You must claim land that is adjacent to the settlement."),
    SETTLEMENT_LAND_ADJACENT_OTHER("You can't claim land that is too close to another settlement."),
    SETTLEMENT_LAND_CLAIMED("This land has already been claimed."),
    SETTLEMENT_LAND_CLAIMED_MAX("Your settlement can't claim more than &b{0}&7 piece(s) of land."),
    SETTLEMENT_LAND_NOT_CLAIMED("This land has not been claimed."),
    SETTLEMENT_LAND_CLAIM("You have claimed new land &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("You have unclaimed the land from the settlement."),
    SETTLEMENT_LAND_UNCLAIM_ONE("The settlement only has only one claimed land. You can't unclaim more land for the settlement."),
    SETTLEMENT_LAND_WORLDGUARD("You can't claim this region as it is protected by worldguard."),
    SETTLEMENT_LAND_TOWNY("You can't claim this region as it is protected by towny."),
    SETTLEMENT_LAND_DISABLED_WORLD("You are not allowed to claim land in this world."),
    SETTLEMENT_LEAVE("You have left the settlement."),
    SETTLEMENT_LEAVE_PLAYER("&b{0} &7has left the settlement."),
    SETTLEMENT_LEAVE_OWNER("You are not able to leave the settlement because you are the owner. You must transfer ownership first."),
    SETTLEMENT_NULL("The settlement &b{0} &7does not seem to exist."),
    SETTLEMENT_NOT_CITIZEN("The player is not a citizen of your settlement."),
    SETTLEMENT_OWNER("You have become the owner of your settlement."),
    SETTLEMENT_NOT_OWNER("You must be owner to proceed with this action."),
    SETTLEMENT_NOT_OWNER_TRANSFER("You must be the settlement owner to transfer ownership."),
    SETTLEMENT_IS_OWNER("You are already the owner of the settlement."),
    SETTLEMENT_IS_MEMBER("The player &b{0} &7is already part of your settlement."),
    SETTLEMENT_IS_TRUSTED("The player &b{0} &7is already trusted."),
    SETTLEMENT_OWNER_TRANSFERRED("You have transferred ownership of your settlement to &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("You are already part of a settlement."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("The player &b{0} &7already has a settlement."),
    SETTLEMENT_PLAYER_NULL("You do not seem to belong to a settlement."),
    SETTLEMENT_ROLE_CREATE("Successfully created the &b{0} &7role."),
    SETTLEMENT_ROLE_DELETE("Successfully deleted the &b{0} &7role."),
    SETTLEMENT_ROLE_EXISTS("The settlement already has the &b{0} &7role."),
    SETTLEMENT_ROLE_NULL("The role you are trying to delete doesn't exist."),
    SETTLEMENT_ROLE_ONE("The settlement must have at least one role available."),
    SETTLEMENT_SET_SPAWN("You have set the spawn for the settlement at your location."),
    SETTLEMENT_SPAWN("You have been teleported to the settlement spawn."),
    SETTLEMENT_SPAWN_NULL("The spawn for the settlement has been set."),
    SETTLEMENT_TELEPORT("You have teleported to &7x: &b{0}&7, z: &b{1}"),

    SETTLEMENT_TRUST("The player &b{0} &7has been trusted."),
    SETTLEMENT_TRUST_SELF("You can't trust yourself?!?"),
    SETTLEMENT_TRUST_REMOVE("The player &b{0} &7is not longer trusted."),
    SETTLEMENT_LIST_NULL("No settlements have been created."),

    // Notifications
    NOTIFICATION_TITLE_HEADER("{0}&l{1}"),
    NOTIFICATION_TITLE_FOOTER("&7{0}"),
    NOTIFICATION_BOSS_BAR("&7Entered {0}&l{1}"),
    NOTIFICATION_ACTION_BAR("&7Entered {0}&l{1}"),
    NOTIFICATION_WILDERNESS_TITLE_HEADER("&a&lWilderness"),
    NOTIFICATION_WILDERNESS_TITLE_FOOTER("&7Fresh new land awaits you"),
    NOTIFICATION_WILDERNESS_BOSS_BAR("&7Entered the &a&lWilderness"),
    NOTIFICATION_WILDERNESS_ACTION_BAR("&7Entered the &a&lWilderness"),

    // Helps pages
    HELP_PAGE_HEADER("&e&lSettlement Help: &a{0}&2/&a{1}"),
    HELP_PAGE_FORMAT("  &b{0}. &f{1}"),
    HELP_PAGE_LIMIT("There are only &b{0} &7pages available."),
    HELP_PAGE_NEXT(">>"),
    HELP_PAGE_PREVIOUS("<<"),
    HELP_PAGE_DISABLED("&7"),
    HELP_PAGE_ENABLED("&e"),

    // Invitations
    INVITATION_DENIED("You have denied an invitation for &b{0}&7."),
    INVITATION_SELF("You can't invite yourself."),
    INVITATION_NULL("You do not have an invitation for &b{0}&7."),
    INVITATION_RECEIVED("You have been invited to join &b{0}&7. Do you wish to accept?"),
    INVITATION_SENT("You have sent an invitation to &b{0}&7."),
    INVITATION_ACCEPT("&a[Accept]"),
    INVITATION_DENY("&c[Deny]"),
    NO_PERMISSIONS("You do not have permissions."),
    RELOAD("You have successfully reloaded all the configurations."),
    UNKNOWN_SUBCOMMAND("&b{0} &7is not a valid subcommand."),
    PLAYER_NULL("The player &b{0} &7has never played before."),
    PLAYER_OFFLINE("The player &b{0} &7does not seem to be online."),
    PLAYER_TELEPORT("You have been teleported to &b{0}&7.");

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