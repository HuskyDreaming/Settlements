package com.huskydreaming.settlements.services.implementations;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SettlementServiceImpl implements SettlementService {

    private Set<Settlement> settlements = new HashSet<>();

    @Override
    public void createSettlement(Player player, String name) {
        settlements.add(Settlement.create(player, name));
    }

    @Override
    public void disbandSettlement(Settlement settlement) {
        settlements.removeIf(s -> s.getName().equalsIgnoreCase(settlement.getName()));
    }

    @Override
    public boolean hasSettlement(Player player) {
        return settlements.stream().anyMatch(settlement -> settlement.isCitizen(player));
    }

    @Override
    public boolean isSettlement(String name) {
        return settlements.stream().anyMatch(settlement -> settlement.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean isSettlement(Chunk chunk) {
        return settlements.stream().anyMatch(settlement -> settlement.isClaimed(chunk));
    }

    @Override
    public Settlement getSettlement(Chunk chunk) {
        return settlements.stream().filter(settlement -> settlement.isClaimed(chunk)).findFirst().orElse(null);
    }

    @Override
    public Settlement getSettlement(String string) {
        return settlements.stream().filter(settlement -> settlement.getName().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    @Override
    public Settlement getSettlement(Player player) {
        return settlements.stream().filter(settlement -> settlement.isCitizen(player)).findFirst().orElse(null);
    }

    @Override
    public Set<Settlement> getSettlements() {
        return Collections.unmodifiableSet(settlements);
    }

    @Override
    public void serialize(SettlementPlugin plugin) {
        Json.write(plugin, "settlements", settlements);
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        Type type = new TypeToken<Set<Settlement>>(){}.getType();
        Set<Settlement> settlements = Json.read(plugin, "settlements", type);
        if(settlements == null) settlements = new HashSet<>();

        this.settlements = settlements;
    }
}
