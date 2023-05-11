package com.huskydreaming.settlements.persistence.roles;

import java.util.*;

public enum RoleDefault {
    CITIZEN(RolePermission.SPAWN_TELEPORT),
    ELDER(
            RolePermission.MEMBER_FRIENDLY_FIRE,
            RolePermission.MEMBER_INVITE,
            RolePermission.LAND_BREAK,
            RolePermission.LAND_PLACE,
            RolePermission.LAND_INTERACT
    ),
    ROYAL(
            RolePermission.MEMBER_KICK,
            RolePermission.MEMBER_KICK_EXEMPT,
            RolePermission.LAND_CLAIM,
            RolePermission.LAND_UNCLAIM
    ),
    MAJESTY(
            RolePermission.EDIT_LAND,
            RolePermission.EDIT_ROLES,
            RolePermission.EDIT_CITIZENS,
            RolePermission.EDIT_SPAWN,
            RolePermission.SPAWN_SET
    );

    private final RolePermission[] rolePermissions;

    RoleDefault(RolePermission... rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Role build() {
        return new Role(this);
    }

    public Set<RolePermission> getRolePermissions() {
        return new HashSet<>(Arrays.asList(rolePermissions));
    }
}
