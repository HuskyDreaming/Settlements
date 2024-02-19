package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigServiceImpl implements ConfigService {

    @Override
    public void deserialize(SettlementPlugin plugin) {
        plugin.saveDefaultConfig();
    }

    @Override
    public List<Role> deserializeDefaultRoles(Plugin plugin) {
        List<Role> defaultRoles = new ArrayList<>();

        String path = "default-roles";
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection(path);
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Role role = new Role(key);
                List<String> permissions = configuration.getStringList(path + "." + key);
                permissions.forEach(p -> role.add(RolePermission.valueOf(p)));
                defaultRoles.add(role);
            }

            int defaultRolesSize = defaultRoles.size();
            if (defaultRolesSize > 0) {
                plugin.getLogger().info("Registered " + defaultRolesSize + " default roles(s).");
            }
        }
        return defaultRoles;
    }

    @Override
    public List<String> deserializeDisabledWorlds(Plugin plugin) {
        String path = "disabled-worlds";
        FileConfiguration configuration = plugin.getConfig();
        return configuration.getStringList(path);
    }

    @Override
    public Map<String, Integer> deserializeDefaultMaximum(Plugin plugin) {
        String path = "default-maximum";
        Map<String, Integer> defaultMaximums = new ConcurrentHashMap<>();
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection(path);
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                int amount = configuration.getInt(path + "." + key);
                defaultMaximums.put(key, amount);
            }
            return defaultMaximums;
        }
        return null;
    }
}