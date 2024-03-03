package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.transience.BorderData;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface BorderService extends ServiceInterface {

    void addPlayer(Player player, String name, Color color);

    void removePlayer(Player player);

    BorderData calculatePositions(String name, Color color);
}