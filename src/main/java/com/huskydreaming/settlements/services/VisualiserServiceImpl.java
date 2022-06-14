package com.huskydreaming.settlements.services;

import com.google.inject.Inject;
import com.huskydreaming.settlements.utilities.Visualise;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VisualiserServiceImpl implements VisualisationService {

    @Inject
    private SettlementService settlementService;

    private final Set<UUID> visualisers = new HashSet<>();

    @Inject

    @Override
    public void runVisualiser(Plugin plugin) {
        Server server = plugin.getServer();
        server.getScheduler().runTaskTimer(plugin, ()->
                server.getOnlinePlayers().forEach(player -> {
                    if(visualisers.contains(player.getUniqueId())) {
                        Visualise.render(player, settlementService.getSettlement(player));
                    }
                }), 0L, 20L
        );
    }

    @Override
    public void addVisualiser(Player player) {
        visualisers.add(player.getUniqueId());
    }

    @Override
    public void removeVisualiser(Player player) {
        visualisers.remove(player.getUniqueId());
    }

    @Override
    public boolean isVisualiser(Player player) {
        return visualisers.contains(player.getUniqueId());
    }
}
