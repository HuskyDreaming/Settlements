package com.huskydreaming.settlements.persistence.roles;

import org.apache.commons.lang.WordUtils;

public enum RolePermission {
    // LAND
    LAND_BREAK(RolePermissionType.LAND),
    LAND_PLACE(RolePermissionType.LAND),
    LAND_INTERACT(RolePermissionType.LAND),
    LAND_CLAIM(RolePermissionType.LAND),
    LAND_UNCLAIM(RolePermissionType.LAND),

    // EDIT
    EDIT_CITIZENS(RolePermissionType.EDIT),
    EDIT_LAND(RolePermissionType.EDIT),
    EDIT_SPAWN(RolePermissionType.EDIT),
    EDIT_ROLES(RolePermissionType.EDIT),

    // MEMBER
    MEMBER_KICK(RolePermissionType.MEMBER),
    MEMBER_INVITE(RolePermissionType.MEMBER),
    MEMBER_KICK_EXEMPT(RolePermissionType.MEMBER),
    MEMBER_FRIENDLY_FIRE(RolePermissionType.MEMBER),

    //SPAWN
    SPAWN_SET(RolePermissionType.SPAWN),
    SPAWN_TELEPORT(RolePermissionType.SPAWN);

    RolePermission(RolePermissionType type) {
        this.type = type;
    }

    private final RolePermissionType type;

    public String getName() {
        return WordUtils.capitalizeFully(name().toLowerCase()).replace("_", " ");
    }

    public RolePermissionType getType() {
        return type;
    }
}
