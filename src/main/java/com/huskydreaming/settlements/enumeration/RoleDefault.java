package com.huskydreaming.settlements.enumeration;

import java.util.List;
public enum RoleDefault {
    CITIZEN(RolePermission.SPAWN_TELEPORT,
            RolePermission.CLAIM_BREAK,
            RolePermission.CLAIM_PLACE),
    VETERAN(RolePermission.SPAWN_TELEPORT,
            RolePermission.MEMBER_INVITE,
            RolePermission.CLAIM_BREAK,
            RolePermission.CLAIM_PLACE,
            RolePermission.CLAIM_INTERACT),
    ROYAL(RolePermission.SPAWN_TELEPORT,
            RolePermission.MEMBER_INVITE,
            RolePermission.MEMBER_KICK,
            RolePermission.MEMBER_KICK_EXEMPT,
            RolePermission.CLAIM_BREAK,
            RolePermission.CLAIM_PLACE,
            RolePermission.CLAIM_INTERACT);

    private final List<RolePermission> rolePermissions;

    RoleDefault(RolePermission... rolePermissions) {
        this.rolePermissions = List.of(rolePermissions);
    }

    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}