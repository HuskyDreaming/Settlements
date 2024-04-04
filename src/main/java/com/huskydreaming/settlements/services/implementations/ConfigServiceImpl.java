package com.huskydreaming.settlements.services.implementations;


import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.enumeration.*;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.services.interfaces.ConfigService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigServiceImpl implements ConfigService {

    private Config config;
    public ConfigServiceImpl(HuskyPlugin plugin) {
        config = Json.read(plugin, "config", Config.class);
        if(config == null) {
            config = new Config();
            config.setFlags(List.of(Flag.ANIMAL_SPAWNING, Flag.MONSTER_SPAWNING));
            config.setNotificationType(NotificationType.TITLE);
            config.setDisabledWorlds(List.of("world_nether", "world_the_end"));
            config.setEmptyPlaceholder("-");
            config.setTeleportation(true);
            config.setTrusting(true);

            Map<SettlementDefaultType, Integer> settlementsDefaults = new ConcurrentHashMap<>();
            for(SettlementDefaultType settlementDefaultType : SettlementDefaultType.values()) {
                settlementsDefaults.put(settlementDefaultType, settlementDefaultType.getValue());
            }
            config.setSettlementDefaults(settlementsDefaults);

            Map<String, List<RolePermission>> roleDefaults = new HashMap<>();
            for(RoleDefault roleDefault : RoleDefault.values()) {
                roleDefaults.put(roleDefault.toString(), roleDefault.getRolePermissions());
            }

            config.setDefaultRoles(roleDefaults);
            Json.write(plugin, "config", config);
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "config", config);
    }

    @Override
    public void selectNotificationType(NotificationType notificationType) {
        List<NotificationType> notificationTypes = List.of(NotificationType.values());
        int index = notificationTypes.indexOf(notificationType);
        if (index < notificationTypes.size() - 1) {
            index += 1;
        } else {
            index = 0;
        }

        config.setNotificationType(notificationTypes.get(index));
    }

    @Override
    public Config getConfig() {
        return config;
    }
}