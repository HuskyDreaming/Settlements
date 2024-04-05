package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SettlementServiceImpl implements SettlementService {

    private final ConfigService configService;
    private Map<String, Settlement> settlements = new ConcurrentHashMap<>();

    public SettlementServiceImpl(SettlementPlugin plugin) {
        configService = plugin.provide(ConfigService.class);
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/settlements", settlements);
        plugin.getLogger().info("Saved " + settlements.size() + " settlement(s).");
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type hashmap = new TypeToken<Map<String, Settlement>>() {}.getType();
        settlements = Json.read(plugin, "data/settlements", hashmap);
        if (settlements == null) settlements = new ConcurrentHashMap<>();

        int size = settlements.size();
        if (size > 0) plugin.getLogger().info("Registered " + size + " settlement(s).");
    }

    @Override
    public Settlement createSettlement(Player player, String name) {
        Settlement settlement = Settlement.create(player);
        Config config = configService.getConfig();

        settlement.setMaxCitizens(config.getSettlementDefault(SettlementDefaultType.MAX_MEMBERS));
        settlement.setMaxLand(config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS));
        settlement.setMaxRoles(config.getSettlementDefault(SettlementDefaultType.MAX_ROLES));

        settlements.put(name, settlement);
        return settlement;
    }

    @Override
    public void disbandSettlement(String name) {
        settlements.remove(name);
    }

    @Override
    public boolean isSettlement(String name) {
        return settlements.containsKey(name.toLowerCase());
    }

    @Override
    public Settlement getSettlement(String string) {
        return settlements.get(string.toLowerCase());
    }

    @Override
    public Map<String, Settlement> getSettlements() {
        return Collections.unmodifiableMap(settlements);
    }
}