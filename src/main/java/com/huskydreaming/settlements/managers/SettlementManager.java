package com.huskydreaming.settlements.managers;

import com.google.gson.reflect.TypeToken;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.storage.Json;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SettlementManager {

    private Set<Settlement> settlements = new HashSet<>();

    public void serialize(Plugin plugin) {
        Json.write(plugin, "settlements", settlements);
    }

    public void deserialize(JavaPlugin plugin) {
        Type type = new TypeToken<Set<Settlement>>(){}.getType();
        Set<Settlement> settlements = Json.read(plugin, "settlements", type);
        if(settlements == null) settlements = new HashSet<>();

        this.settlements = settlements;
    }

    public boolean isClaimed(Chunk chunk) {
        return settlements.stream().anyMatch(settlement -> settlement.isClaimed(chunk));
    }

    public Settlement getSettlement(Chunk chunk) {
        return settlements.stream().filter(settlement -> settlement.isClaimed(chunk)).findFirst().orElse(null);
    }

    public Settlement getSettlement(String string) {
        return settlements.stream().filter(settlement -> settlement.getName().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public Settlement getSettlement(Player player) {
        return settlements.stream().filter(settlement -> settlement.isCitizen(player)).findFirst().orElse(null);
    }

    public boolean hasSettlement(Player player) {
        return settlements.stream().anyMatch(settlement -> settlement.isCitizen(player));
    }

    public void create(Player player, String name) {
        settlements.add(Settlement.create(player, name));
    }

    public boolean exists(String name) {
        return settlements.stream().anyMatch(settlement -> settlement.getName().equalsIgnoreCase(name));
    }

    public void disband(Settlement settlement) {
        settlements.removeIf(s -> s.getName().equalsIgnoreCase(settlement.getName()));
    }

    public Set<Settlement> getSettlements() {
        return Collections.unmodifiableSet(settlements);
    }
}
