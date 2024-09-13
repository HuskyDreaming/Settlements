package com.huskydreaming.settlements.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.commands.subcommands.*;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.*;
import com.huskydreaming.settlements.enumeration.types.NotificationType;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigServiceImpl implements ConfigService {

    private Config config;

    @Override
    public void serialize(HuskyPlugin plugin) {
        if(config != null) Json.write(plugin, "config", config);
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        config = new Config();
        config.setLocalization("en");
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
    public void setup(HuskyPlugin plugin) {
        CommandRegistry commandRegistry = plugin.getCommandRegistry();
        if(config.isTeleportation()) {
            commandRegistry.add(new SpawnCommand(plugin));
            commandRegistry.add(new SetSpawnCommand(plugin));
        }

        if(config.isTrusting()) {
            commandRegistry.add(new TrustCommand(plugin));
            commandRegistry.add(new UnTrustCommand(plugin));
        }

        if(config.isHomes()) {
            commandRegistry.add(new DeleteHomeCommand(plugin));
            commandRegistry.add(new HomeCommand(plugin));
            commandRegistry.add(new SetHomeCommand(plugin));
        }
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public boolean setupLanguage(HuskyPlugin plugin) {
        Type type = new TypeToken<Config>(){}.getType();
        config = Json.read(plugin, "config", type);
        if (config == null) {
            config = new Config();
            return true;
        }
        return false;
    }

    @Override
    public void setupConfig(HuskyPlugin plugin) {
        config.setFlags(List.of(FlagType.ANIMAL_SPAWNING, FlagType.MONSTER_SPAWNING));
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

        Map<String, List<PermissionType>> roleDefaults = new HashMap<>();
        for (RoleDefault roleDefault : RoleDefault.values()) {
            roleDefaults.put(roleDefault.toString(), roleDefault.getRolePermissions());
        }

        config.setDefaultRoles(roleDefaults);
        Json.write(plugin, "config", config);
    }
}