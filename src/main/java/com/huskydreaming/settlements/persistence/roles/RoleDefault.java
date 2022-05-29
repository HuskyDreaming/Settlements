package com.huskydreaming.settlements.persistence.roles;

import java.util.*;

public enum RoleDefault {
    MAJESTY("ROYAL",
            RolePermission.EDIT_LAND,
            RolePermission.EDIT_ROLES,
            RolePermission.EDIT_CITIZENS,
            RolePermission.EDIT_SPAWN,
            RolePermission.SPAWN_SET
    ),
    ROYAL("ELDER",
            RolePermission.CITIZEN_KICK,
            RolePermission.CITIZEN_KICK_EXEMPT,
            RolePermission.LAND_CLAIM,
            RolePermission.LAND_UNCLAIM
    ),
    ELDER("CITIZEN",
            RolePermission.CITIZEN_FRIENDLY_FIRE,
            RolePermission.CITIZEN_INVITE,
            RolePermission.LAND_BREAK,
            RolePermission.LAND_PLACE,
            RolePermission.LAND_INTERACT
    ),
    CITIZEN(null,
            RolePermission.SPAWN_TELEPORT);

    private final RolePermission[] rolePermissions;
    private final String parent;

    RoleDefault(String parent, RolePermission... rolePermissions) {
        this.parent = parent;
        this.rolePermissions = rolePermissions;
    }

    public Role build() {
        return new Role(this);
    }

    public String getParent() {
        return parent;
    }

    public Set<RolePermission> getRolePermissions() {
        return new HashSet<>(Arrays.asList(rolePermissions));
    }
}
