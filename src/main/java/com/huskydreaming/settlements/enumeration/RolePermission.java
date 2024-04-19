package com.huskydreaming.settlements.enumeration;

import com.huskydreaming.huskycore.interfaces.Permission;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.storage.types.Locale;

public enum RolePermission implements Permission {
    DEFAULT(""),
    // CLAIMS
    CLAIM_BREAK(Locale.PERMISSIONS_CLAIM_BREAK.parse()),
    CLAIM_PLACE(Locale.PERMISSIONS_CLAIM_PLACE.parse()),
    CLAIM_INTERACT(Locale.PERMISSIONS_CLAIM_INTERACT.parse()),
    CLAIM_LAND(Locale.PERMISSIONS_CLAIM_LAND.parse()),
    CLAIM_TELEPORT(Locale.PERMISSIONS_CLAIM_TELEPORT.parse()),

    // EDIT
    EDIT_MEMBERS(Locale.PERMISSIONS_EDIT_MEMBERS.parse()),
    EDIT_CLAIMS(Locale.PERMISSIONS_EDIT_CLAIMS.parse()),
    EDIT_HOMES(Locale.PERMISSIONS_EDIT_HOMES.parse()),
    EDIT_SPAWN(Locale.PERMISSIONS_EDIT_SPAWN.parse()),
    EDIT_ROLES(Locale.PERMISSIONS_EDIT_ROLES.parse()),
    EDIT_FLAGS(Locale.PERMISSIONS_EDIT_FLAGS.parse()),
    EDIT_DESCRIPTION(Locale.PERMISSIONS_EDIT_DESCRIPTION.parse()),
    EDIT_TAGS(Locale.PERMISSIONS_EDIT_TAGS.parse()),

    // MEMBER
    MEMBER_KICK(Locale.PERMISSIONS_MEMBER_KICK.parse()),
    MEMBER_TRUST(Locale.PERMISSIONS_MEMBER_TRUST.parse()),
    MEMBER_INVITE(Locale.PERMISSIONS_MEMBER_INVITE.parse()),
    MEMBER_KICK_EXEMPT(Locale.PERMISSIONS_MEMBER_KICK_EXEMPT.parse()),
    HOME_TELEPORT(Locale.PERMISSIONS_HOME_TELEPORT.parse()),
    SPAWN_TELEPORT(Locale.PERMISSIONS_SPAWN_TELEPORT.parse());

    private final String description;

    RolePermission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Util.capitalize(name().toLowerCase().replace("_", " "));
    }
}