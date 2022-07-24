package com.huskydreaming.settlements.utilities;

import org.bukkit.configuration.file.FileConfiguration;

public enum Locale {
    PREFIX("&a&lSettlements: &7"),
    SETTLEMENT_CREATED("You have created a new settlement named &b{0}&7."),
    SETTLEMENT_DISBAND("You have disbanded your settlement."),
    SETTLEMENT_ESTABLISHED("A settlement has already been established here."),
    SETTLEMENT_EXIST("A settlement with that name already exists."),
    SETTLEMENT_JOIN("You have joined the &b{0} &7settlement."),
    SETTLEMENT_KICK("You have been kicked from the settlement."),
    SETTLEMENT_KICK_EXEMPT("This player can't be kicked from the settlement."),
    SETTLEMENT_LAND_CLAIMED("This land has been claimed by another settlement"),
    SETTLEMENT_LAND_NOT_CLAIMED("This land has not been claimed."),
    SETTLEMENT_LAND_CLAIM("You have claimed new land &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("You have unclaimed the land from the settlement."),
    SETTLEMENT_NULL("The settlement &b{0} &7does not seem to exist."),
    SETTLEMENT_NOT_CITIZEN("The player is not a citizen of your settlement."),
    SETTLEMENT_OWNER("You must be owner to proceed with this action."),
    SETTLEMENT_OWNER_TRANSFERRED("You have transferred ownership of your settlement to &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("You are already part of a settlement."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("The player &b{0} &7already has a settlement."),
    SETTLEMENT_PLAYER_NULL("You do not seem to belong to a settlement."),
    SETTLEMENT_SET_SPAWN("You have set the spawn for the settlement at your location."),
    SETTLEMENT_SPAWN("You have been teleported to the settlement spawn."),
    INVITATION_DENIED("You have denied an invitation for &b{0}&7."),
    INVITATION_NULL("You do not have an invitation for &b{0}&7."),
    INVITATION_SENT("You have sent an invitation to &f{0}&7."),
    NO_PERMISSIONS("You do not have permissions for: &f{0}"),
    PLAYER_NULL("The player &f{0} &7has never played before."),
    PLAYER_OFFLINE("The player &f{0} &7does not seem to be online.");

    private final String def;
    private static FileConfiguration localeConfiguration;

    Locale(String def) {
        this.def = def;
    }

    public String parse() {
        return localeConfiguration.getString(toString(), def);
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}
