package com.huskydreaming.settlements.persistence.roles;

import com.huskydreaming.settlements.utilities.Remote;

import java.util.*;

public class Role {
    private final String name;
    private final Set<RolePermission> permissions;

    public static Role create(String name) {
        return new Role(name);
    }

    public Role(String name) {
        this.name = name;
        this.permissions = new HashSet<>();
    }

    public void add(RolePermission permission) {
        permissions.add(permission);
    }

    public void remove(RolePermission permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(RolePermission permission) {
        return permissions.contains(permission);
    }

    public String getName() {
        return Remote.capitalizeFully(name.toLowerCase());
    }
}
