package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.enumeration.*;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigServiceImpl implements ConfigService {

    private Config config;

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "config", config);
    }

    @Override
    public boolean isDisabledWorld(Player player) {
        World world = player.getWorld();
        if (config.containsDisableWorld(world) || world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(Message.LAND_DISABLED_WORLD.prefix());
            return true;
        }
        return false;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public Config setupLanguage(HuskyPlugin plugin) {
        config = Json.read(plugin, "config", Config.class);
        if (config != null) return config;

        config = new Config();
        config.setLocalization("en");
        return config;
    }

    @Override
    public void setupConfig(HuskyPlugin plugin, Config config) {
        config.setFlags(List.of(Flag.ANIMAL_SPAWNING, Flag.MONSTER_SPAWNING));
        config.setNotificationType(NotificationType.TITLE);
        config.setDisabledWorlds(List.of("world_nether", "world_the_end"));
        config.setEmptyPlaceholder("-");
        config.setTeleportation(true);
        config.setHomes(true);
        config.setTrusting(true);

        Map<SettlementDefaultType, Integer> settlementsDefaults = new ConcurrentHashMap<>();
        for (SettlementDefaultType settlementDefaultType : SettlementDefaultType.values()) {
            settlementsDefaults.put(settlementDefaultType, settlementDefaultType.getValue());
        }
        config.setSettlementDefaults(settlementsDefaults);

        Map<String, List<RolePermission>> roleDefaults = new HashMap<>();
        for (RoleDefault roleDefault : RoleDefault.values()) {
            roleDefaults.put(roleDefault.toString(), roleDefault.getRolePermissions());
        }

        config.setDefaultRoles(roleDefaults);
        Json.write(plugin, "config", config);
    }
}