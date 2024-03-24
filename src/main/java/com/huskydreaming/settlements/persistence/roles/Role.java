package com.huskydreaming.settlements.persistence.roles;

import com.huskydreaming.huskycore.utilities.Util;

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
        return Util.capitalize(name.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(name, role.name) && Objects.equals(permissions, role.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, permissions);
    }
}