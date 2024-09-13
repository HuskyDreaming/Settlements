package com.huskydreaming.settlements.enumeration;

import java.util.List;
public enum RoleDefault {
    CITIZEN(PermissionType.SPAWN_TELEPORT,
            PermissionType.CLAIM_BREAK,
            PermissionType.CLAIM_PLACE),
    VETERAN(PermissionType.SPAWN_TELEPORT,
            PermissionType.MEMBER_INVITE,
            PermissionType.CLAIM_BREAK,
            PermissionType.CLAIM_PLACE,
            PermissionType.CLAIM_INTERACT),
    ROYAL(PermissionType.SPAWN_TELEPORT,
            PermissionType.MEMBER_INVITE,
            PermissionType.MEMBER_KICK,
            PermissionType.MEMBER_KICK_EXEMPT,
            PermissionType.CLAIM_BREAK,
            PermissionType.CLAIM_PLACE,
            PermissionType.CLAIM_INTERACT);

    private final List<PermissionType> permissionTypes;

    RoleDefault(PermissionType... permissionTypes) {
        this.permissionTypes = List.of(permissionTypes);
    }

    public List<PermissionType> getRolePermissions() {
        return permissionTypes;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}