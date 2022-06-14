package com.huskydreaming.settlements.services;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface VisualisationService {

    void runVisualiser(Plugin plugin);

    void addVisualiser(Player player );

    void removeVisualiser(Player player);

    boolean isVisualiser(Player player);
}
