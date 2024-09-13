package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.BorderData;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface BorderService extends Service {

    void addPlayer(Player player, Settlement settlement, Color color);

    void addPlayer(Player player, long settlementId, Color color);

    void removePlayer(Player player);

    BorderData calculatePositions(long settlementId, Color color);
}