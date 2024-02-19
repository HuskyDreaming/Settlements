package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.ConfigService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettlementServiceImpl implements SettlementService {

    private final ConfigService configService;
    private Set<Settlement> settlements = new HashSet<>();
    private Map<String, Integer> defaults;

    public SettlementServiceImpl() {
        configService = ServiceProvider.Provide(ConfigService.class);
    }

    @Override
    public Settlement createSettlement(Player player, String name) {
        Settlement settlement = Settlement.create(player, name);
        settlements.add(settlement);
        if(defaults != null) {
            settlement.setMaxCitizens(defaults.get("members"));
            settlement.setMaxLand(defaults.get("claims"));
            settlement.setMaxRoles(defaults.get("roles"));
        }
        return settlement;
    }

    @Override
    public void disbandSettlement(Settlement settlement) {
        settlements.removeIf(s -> s.getName().equalsIgnoreCase(settlement.getName()));
    }

    @Override
    public boolean isSettlement(String name) {
        return settlements.stream().anyMatch(settlement -> settlement.getName().equalsIgnoreCase(name));
    }

    @Override
    public Settlement getSettlement(String string) {
        return settlements.stream().filter(settlement -> settlement.getName().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    @Override
    public Set<Settlement> getSettlements() {
        return Collections.unmodifiableSet(settlements);
    }

    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "data/settlements", settlements);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Set<Settlement>>(){}.getType();
        settlements = Json.read(plugin, "data/settlements", type);
        if(settlements == null) settlements = new HashSet<>();

        int size = settlements.size();
        if(size > 0) {
            plugin.getLogger().info("Registered " + size + " settlement(s).");
        }

        if(defaults != null) defaults.clear();
        defaults = configService.deserializeDefaultMaximum(plugin);
    }
}