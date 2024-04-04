package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.RoleService;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleServiceImpl implements RoleService {

    private final ConfigService configService;

    private Map<String, List<Role>> roles = new HashMap<>();

    public RoleServiceImpl(SettlementPlugin plugin) {
        configService = plugin.provide(ConfigService.class);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/roles", roles);
        plugin.getLogger().info("Saved " + roles.size() + " roles(s).");
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<String, List<Role>>>() {}.getType();
        roles = Json.read(plugin, "data/roles", type);

        if (roles == null) roles = new ConcurrentHashMap<>();

        int rolesSize = roles.size();
        if (rolesSize > 0) {
            plugin.getLogger().info("Registered " + rolesSize + " roles(s).");
        }
    }

    @Override
    public void clean(String settlementName) {
        roles.remove(settlementName);
    }

    @Override
    public void setup(String settlementName, Settlement settlement) {
        Config config = configService.getConfig();
        List<Role> defaultRoles = new ArrayList<>();

        config.getDefaultRoles().forEach((s, rolePermissions) -> {
            Role role = Role.create(s);

            for(RolePermission rolePermission : rolePermissions) {
                role.add(rolePermission);
            }

            defaultRoles.add(role);
        });

        this.roles.put(settlementName, defaultRoles);
        settlement.setDefaultRole(defaultRoles.stream().findFirst().map(Role::getName).orElse(null));
    }

    @Override
    public List<Role> getRoles(String settlementName) {
        return roles.get(settlementName);
    }

    @Override
    public Role getRole(Member member) {
        return roles.get(member.getSettlement()).stream()
                .filter(role -> role.getName().equalsIgnoreCase(member.getRole()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getRole(String settlementName, String name) {
        return roles.get(settlementName).stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getOtherRole(String settlementName, String name) {
        return roles.get(settlementName).stream()
                .filter(role -> !role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remove(String settlementName, Role role) {
        roles.get(settlementName).removeIf(r -> r.getName().equalsIgnoreCase(role.getName()));
    }

    @Override
    public void add(String settlementName, String name) {
        roles.get(settlementName).add(Role.create(name));
    }

    @Override
    public boolean promote(String settlementName, Role role, Member member) {
        List<Role> list = roles.get(settlementName);
        int index = list.indexOf(role);
        if (index < list.size() - 1) {
            role = list.get(index + 1);
            if (role != null) {
                member.setRole(role.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean demote(String settlementName, Role role, Member member) {
        List<Role> list = roles.get(settlementName);
        int index = list.indexOf(role);
        if (index >= 1) {
            role = list.get(index - 1);
            if (role != null) {
                member.setRole(role.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRole(String settlementName, String name) {
        return roles.get(settlementName).stream().anyMatch(role -> role.getName().equalsIgnoreCase(name));
    }

    @Override
    public int getIndex(String settlementName, Member member) {
        Role role = getRole(member);
        return roles.get(settlementName).indexOf(role) + 1;
    }

    @Override
    public int getIndex(String settlementName, String name) {
        Role finalRole = roles.get(settlementName).stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return roles.get(settlementName).indexOf(finalRole);
    }

    @Override
    public Role sync(Member member, String defaultRole) {
        List<Role> roles = getRoles(member.getSettlement());
        if (roles.stream().noneMatch(role -> role.getName().equalsIgnoreCase(member.getRole()))) {
            member.setRole(defaultRole);
        }
        return roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase(member.getRole()))
                .findFirst()
                .orElse(null);
    }
}