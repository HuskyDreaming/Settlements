package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.storage.Json;
import com.huskydreaming.settlements.storage.Yaml;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleServiceImpl implements RoleService {

    private Map<String, List<Role>> roles = new HashMap<>();
    private final List<Role> defaultRoles = new ArrayList<>();

    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "data/roles", roles);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Map<String, List<Role>>>() {}.getType();
        roles = Json.read(plugin, "data/roles", type);
        if (roles == null) roles = new ConcurrentHashMap<>();

        int rolesSize = roles.size();
        if (rolesSize > 0) {
            plugin.getLogger().info("Registered " + rolesSize + " roles(s).");
        }

        File file = new File(plugin.getDataFolder(), "default-roles.yml");
        if (!file.exists()) {
            plugin.saveResource("default-roles.yml", false);
        }

        Yaml yaml = new Yaml("default-roles");
        yaml.load(plugin);

        FileConfiguration configuration = yaml.getConfiguration();

        ConfigurationSection configurationSection = configuration.getConfigurationSection("");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Role role = new Role(key);
                List<String> permissions = configuration.getStringList(key);
                permissions.forEach(p -> role.add(RolePermission.valueOf(p)));
                defaultRoles.add(role);
            }

            int defaultRolesSize = defaultRoles.size();
            if (defaultRolesSize > 0) {
                plugin.getLogger().info("Registered " + defaultRolesSize + " default roles(s).");
            }
        }
    }

    @Override
    public void clean(Settlement settlement) {
        roles.remove(settlement.getName());
    }

    @Override
    public void setup(Settlement settlement) {
        this.roles.put(settlement.getName(), defaultRoles);
        settlement.setDefaultRole(defaultRoles.stream().findFirst().map(Role::getName).orElse(null));
    }

    @Override
    public List<Role> getRoles(Settlement settlement) {
        return roles.get(settlement.getName());
    }

    @Override
    public Role getRole(Settlement settlement, Member member) {
        if(!hasRole(settlement, member.getRole()) || member.getRole() == null) {
            Role defaultRole = getDefaultRole(settlement);
            List<Role> roles = getRoles(settlement);
            if(defaultRole == null) {
                settlement.setDefaultRole(roles.stream().map(Role::getName).findFirst().orElse(null));
            }

            member.setRole(settlement.getDefaultRole());
        }

        return roles.get(settlement.getName()).stream()
                .filter(role -> role.getName().equalsIgnoreCase(member.getRole()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getRole(Settlement settlement, String name) {
        return roles.get(settlement.getName()).stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getDefaultRole(Settlement settlement) {
        return roles.get(settlement.getName()).stream()
                .filter(role -> role.getName().equalsIgnoreCase(settlement.getDefaultRole()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Role getOtherRole(Settlement settlement, String name) {
        return roles.get(settlement.getName()).stream()
                .filter(role -> !role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
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
    public boolean hasRole(Settlement settlement, String name) {
        return roles.get(settlement.getName()).stream().anyMatch(role -> role.getName().equalsIgnoreCase(name));
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