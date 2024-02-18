package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface BorderService extends ServiceInterface {

    void addPlayer(Player player, String settlementName, Color color);

    void removePlayer(Player player);

    void run(Plugin plugin);

    BorderData calculatePositions(Settlement settlement, Color color);
}
