package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.storage.transience.BorderData;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface BorderService extends Service {

    void addPlayer(Player player, String name, Color color);

    void removePlayer(Player player);

    BorderData calculatePositions(String name, Color color);
}