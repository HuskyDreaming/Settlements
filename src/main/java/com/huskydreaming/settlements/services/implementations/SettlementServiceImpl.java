package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SettlementServiceImpl implements SettlementService {

    private final ConfigService configService;
    private Map<String, Settlement> settlements = new ConcurrentHashMap<>();
    private Map<String, Integer> defaults;

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
        Type set = new TypeToken<Set<Settlement>>() {}.getType();
        Set<Settlement> settlementSet = Json.read(plugin, "data/settlements", set);

        if (settlementSet != null) {
            // This deprecation is fine as we are converting old data format to new
            settlementSet.forEach(settlement -> settlements.put(settlement.getName(), settlement));

            plugin.getLogger().info("Converted old data format to new enhanced data format...");

            int size = settlements.size();
            if (size > 0) {
                plugin.getLogger().info("Registered " + size + " settlement(s).");
            }
        } else {
            Type hashmap = new TypeToken<Map<String, Settlement>>() {
            }.getType();
            settlements = Json.read(plugin, "data/settlements", hashmap);
            if (settlements == null) settlements = new ConcurrentHashMap<>();

            int size = settlements.size();
            if (size > 0) plugin.getLogger().info("Registered " + size + " settlement(s).");
        }

        if (defaults != null) defaults.clear();
        defaults = configService.deserializeDefaults(plugin);
    }

    @Override
    public Settlement createSettlement(Player player, String name) {
        Settlement settlement = Settlement.create(player);
        if (defaults != null) {
            settlement.setMaxCitizens(defaults.getOrDefault("max-members", 10));
            settlement.setMaxLand(defaults.getOrDefault("max-claims", 15));
            settlement.setMaxRoles(defaults.getOrDefault("max-roles", 5));
        }

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

    @Override
    public Map<String, Integer> getDefaults() {
        return Collections.unmodifiableMap(defaults);
    }
}