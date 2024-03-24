package com.huskydreaming.settlements.persistence.roles;

import com.huskydreaming.huskycore.utilities.Util;

public enum RolePermission {
    // LAND
    LAND_BREAK,
    LAND_PLACE,
    LAND_INTERACT,
    LAND_CLAIM,
    LAND_UNCLAIM,

    // EDIT
    EDIT_CITIZENS,
    EDIT_LAND,
    EDIT_SPAWN,
    EDIT_ROLES,
    EDIT_FLAGS,
    EDIT_DESCRIPTION,
    EDIT_TAGS,

    // MEMBER
    MEMBER_KICK,
    MEMBER_INVITE,
    MEMBER_KICK_EXEMPT,
    MEMBER_FRIENDLY_FIRE,

    //SPAWN
    SPAWN_SET,
    SPAWN_TELEPORT;

    public String getName() {
        return Util.capitalize(name().toLowerCase()).replace("_", " ");
    }
}