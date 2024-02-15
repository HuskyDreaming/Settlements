package com.huskydreaming.settlements.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Locale implements Parseable {
    PREFIX("&aSettlements: &7"),
    SETTLEMENT_CREATED("You have created a new settlement named &b{0}&7."),
    SETTLEMENT_DESCRIPTION("You have set the settlement description to: &b{0}"),
    SETTLEMENT_DESCRIPTION_LONG("The description can't be greater than &b{0} &7characters."),
    SETTLEMENT_DISBAND("You have disbanded your settlement."),
    SETTLEMENT_ESTABLISHED("A settlement has already been established here."),
    SETTLEMENT_EXIST("A settlement with that name already exists."),
    SETTLEMENT_JOIN("You have joined the &b{0} &7settlement."),
    SETTLEMENT_KICK("You have been kicked from the &b{0} &7settlement."),
    SETTLEMENT_KICK_PLAYER("You have kicked &b{0} &7from the settlement."),
    SETTLEMENT_KICK_EXEMPT("This player can't be kicked from the settlement."),
    SETTLEMENT_LAND_ADJACENT("You must claim land that is adjacent to your current claims."),
    SETTLEMENT_LAND_CLAIMED("This land has already been claimed."),
    SETTLEMENT_LAND_NOT_CLAIMED("This land has not been claimed."),
    SETTLEMENT_LAND_CLAIM("You have claimed new land &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("You have unclaimed the land from the settlement."),
    SETTLEMENT_LAND_UNCLAIM_ONE("You only have one claimed land. You can't unclaim more."),
    SETTLEMENT_NULL("The settlement &b{0} &7does not seem to exist."),
    SETTLEMENT_NOT_CITIZEN("The player is not a citizen of your settlement."),
    SETTLEMENT_OWNER("You have become the owner of your settlement."),
    SETTLEMENT_NOT_OWNER("You must be owner to proceed with this action."),
    SETTLEMENT_NOT_OWNER_TRANSFER("You must be the settlement owner to transfer ownership."),
    SETTLEMENT_IS_OWNER("You are already the owner of the settlement."),
    SETTLEMENT_OWNER_TRANSFERRED("You have transferred ownership of your settlement to &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("You are already part of a settlement."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("The player &b{0} &7already has a settlement."),
    SETTLEMENT_PLAYER_NULL("You do not seem to belong to a settlement."),
    SETTLEMENT_SET_SPAWN("You have set the spawn for the settlement at your location."),
    SETTLEMENT_SPAWN("You have been teleported to the settlement spawn."),
    SETTLEMENT_TELEPORT("You have teleported to &7x: &b{0}&7, z: &b{1}"),
    INVITATION_DENIED("You have denied an invitation for &b{0}&7."),
    INVITATION_SELF("You can't invite yourself."),
    INVITATION_NULL("You do not have an invitation for &b{0}&7."),
    INVITATION_RECEIVED("You have been invited to join &b{0}&7. Do you wish to accept?"),
    INVITATION_SENT("You have sent an invitation to &b{0}&7."),
    INVITATION_ACCEPT("&a[Accept]"),
    INVITATION_DENY("&c[Deny]"),
    NO_PERMISSIONS("You do not have permissions for: &b{0}"),
    PLAYER_NULL("The player &b{0} &7has never played before."),
    PLAYER_OFFLINE("The player &b{0} &7does not seem to be online.");

    private final String def;
    private static FileConfiguration localeConfiguration;

    Locale(String def) {
        this.def = def;
    }

    public String parse() {
        return ChatColor.translateAlternateColorCodes('&', localeConfiguration.getString(toString(), def));
    }

    @Override
    public List<String> parseList() {
        return null;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}
