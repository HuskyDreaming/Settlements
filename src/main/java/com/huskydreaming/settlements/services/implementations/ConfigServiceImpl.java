package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.enumerations.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigServiceImpl implements ConfigService {

    @Override
    public List<Role> deserializeDefaultRoles(Plugin plugin) {
        List<Role> defaultRoles = new ArrayList<>();

        String path = Config.ROLES.toString();
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
        return plugin.getConfig().getStringList(Config.DISABLED_WORLDS.toString());
    }

    @Override
    public String deserializeEmptyPlaceholder(Plugin plugin) {
        return plugin.getConfig().getString(Config.PLACEHOLDER_STRING.toString());
    }

    @Override
    public Map<String, Integer> deserializeDefaults(Plugin plugin) {
        Map<String, Integer> defaults = new ConcurrentHashMap<>();
        FileConfiguration configuration = plugin.getConfig();
        String path = Config.SETTLEMENT.toString();
        ConfigurationSection configurationSection = configuration.getConfigurationSection(path);
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                int amount = configuration.getInt(path + "." + key);
                defaults.put(key, amount);
            }
            return defaults;
        }
        return null;
    }
}