package com.huskydreaming.settlements.persistence.roles;

import com.huskydreaming.settlements.persistence.Settlement;
import org.apache.commons.lang.WordUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Role {

    private String parent;
    private final String name;
    private final Set<RolePermission> permissions;

    public static Role create(String name) {
        return new Role(name);
    }

    // parent is null
    public List<String> getChildren(Settlement settlement) {

        return null;
    }

    public List<String> getChildren(Settlement settlement, List<String> list, String name) {
        for(Role role : settlement.getRoles()) {
            if(role.getParent() == null) continue;
            if(role.getParent().equalsIgnoreCase(name)) {
                list.add(role.getName());
                break;
            }
        }
        return getChildren(settlement, list, name);
    }

    public Role(String name) {
        this.name = name;
        this.permissions = new HashSet<>();
        this.parent = "none";
    }

    public Role(RoleDefault roleDefault) {
        this.parent = roleDefault.getParent();
        this.name = roleDefault.name();
        this.permissions = roleDefault.getRolePermissions();
    }

    public void setParent(String parent) {
        this.parent = parent;
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
        return WordUtils.capitalize(name.toLowerCase());
    }

    public String getParent() {
        return parent;
    }

    public Set<RolePermission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
