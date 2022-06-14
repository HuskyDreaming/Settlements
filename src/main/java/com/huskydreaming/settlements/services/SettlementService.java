package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public interface SettlementService {

    void serialize(Plugin plugin);

    void deserialize(JavaPlugin plugin);

    void createSettlement(Player player, String name);

    void disbandSettlement(Settlement settlement);

    boolean hasSettlement(Player player);

    boolean isSettlement(String name);

    boolean isSettlement(Chunk chunk);

    Settlement getSettlement(Chunk chunk);

    Settlement getSettlement(String string);

    Settlement getSettlement(Player player);

    Set<Settlement> getSettlements();
}
