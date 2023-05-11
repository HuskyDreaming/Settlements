package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RoleDefault;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.storage.Json;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

    private Map<String, List<Role>> roles = new HashMap<>();

    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "data/roles", roles);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<String, List<Role>>>(){}.getType();
        roles = Json.read(plugin, "data/roles", type);
        if(roles == null) roles = new ConcurrentHashMap<>();

        int size = roles.size();
        if(size > 0) {
            plugin.getLogger().info("Registered " + size + " roles(s).");
        }
    }

    @Override
    public void setup(Settlement settlement) {
        List<Role> roles = Arrays.stream(RoleDefault.values())
                .map(RoleDefault::build)
                .collect(Collectors.toList());

        this.roles.put(settlement.getName(), roles);
        settlement.setDefaultRole(RoleDefault.CITIZEN.name());
    }

    @Override
    public List<Role> getRoles(Settlement settlement) {
        return roles.get(settlement.getName());
    }

    @Override
    public Role getRole(Settlement settlement, Member member) {
        List<Role> roles = getRoles(settlement);
        AtomicReference<Role> finalRole = new AtomicReference<>(roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase(member.getRole()))
                .findFirst()
                .orElse(null));

        if(finalRole.get() == null) {
            roles.stream().findFirst().ifPresent(role -> {
                finalRole.set(role);
                member.setRole(role.getName());
            });
        }

        return finalRole.get();
    }

    @Override
    public void remove(Settlement settlement, Role role) {
        roles.get(settlement.getName()).removeIf(r -> r.getName().equalsIgnoreCase(role.getName()));
    }

    @Override
    public void add(Settlement settlement, String name) {
        roles.get(settlement.getName()).add(Role.create(name));
    }

    @Override
    public boolean promote(Settlement settlement, Member member) {
        List<Role> list = roles.get(settlement.getName());
        Role role = sync(list, settlement, member);
        int index = list.indexOf(role);
        if(index < list.size() - 1) {
            role = list.get(index + 1);
            if (role != null) {
                member.setRole(role.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean demote(Settlement settlement, Member member) {
        List<Role> list = roles.get(settlement.getName());
        Role role = sync(list, settlement, member);
        int index = list.indexOf(role);
        if(index >= 1) {
            role = list.get(index - 1);
            if (role != null) {
                member.setRole(role.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public int getIndex(Settlement settlement, Member member) {
        Role role = getRole(settlement, member);
        return roles.get(settlement.getName()).indexOf(role) + 1;
    }

    @Override
    public int getIndex(Settlement settlement, String name) {
        Role finalRole = roles.get(settlement.getName()).stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return roles.get(settlement.getName()).indexOf(finalRole);
    }

    @Override
    public Role sync(List<Role> roles, Settlement settlement, Member member) {
        if(roles.stream().noneMatch(role -> role.getName().equalsIgnoreCase(member.getRole()))) {
            member.setRole(settlement.getDefaultRole());
        }
        return roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase(member.getRole()))
                .findFirst()
                .orElse(null);
    }
}