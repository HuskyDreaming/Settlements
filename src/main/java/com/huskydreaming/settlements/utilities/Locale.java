package com.huskydreaming.settlements.utilities;

public enum Locale {
    SETTLEMENT_CREATED("&7You have created a new settlement named &b{0}&7."),
    SETTLEMENT_DISBAND("&7You have disbanded your settlement."),
    SETTLEMENT_ESTABLISHED("&7A settlement has already been established here."),
    SETTLEMENT_EXIST("&7A settlement with that name already exists."),
    SETTLEMENT_JOIN("&7You have joined the &b{0} &7settlement."),
    SETTLEMENT_LAND_CLAIMED("&7This land has been claimed by another settlement"),
    SETTLEMENT_LAND_CLAIM("&7You have claimed new land &7x: &b{0}&7, z:&b{1}"),
    SETTLEMENT_LAND_UNCLAIM("&7You have unclaimed the land from the settlement."),
    SETTLEMENT_NULL("&7The settlement &b{0} &7does not seem to exist."),
    SETTLEMENT_NOT_CITIZEN("&7The player is not a citizen of your settlement."),
    SETTLEMENT_OWNER("&7You must be owner to proceed with this action."),
    SETTLEMENT_OWNER_TRANSFERRED("&7You have transferred ownership of your settlement to &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("&7You are already part of a settlement."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("&7The player &b{0} &7already has a settlement."),
    SETTLEMENT_PLAYER_NULL("&7You do not seem to belong to a settlement."),
    SETTLEMENT_SET_SPAWN("&7You have set the spawn for the settlement at your location."),
    SETTLEMENT_SPAWN("&7You have been teleported to the settlement spawn."),
    INVITATION_DENIED("&7You have denied an invitation for &b{0}&7."),
    INVITATION_NULL("&7You do not have an invitation for &b{0}&7."),
    INVITATION_SENT("&7You have sent an invitation to &f{0}&7."),
    NO_PERMISSIONS("&7You do not have permissions for: &f{0}"),
    PLAYER_OFFLINE("&7The player &f{0} &7does not seem to be online.");
    private final String string;

    Locale(String string) {
        this.string = string;
    }

    public String parse() {
        return string.toLowerCase().replace("_", "-");
    }
}
