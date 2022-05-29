package com.huskydreaming.settlements.managers;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.utilities.Visualise;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VisualisationManager {

    private final Set<UUID> visualisers = new HashSet<>();

    public void run(Settlements settlements) {
        Server server = settlements.getServer();
        settlements.getServer().getScheduler().runTaskTimer(settlements, ()->
                server.getOnlinePlayers().forEach(player -> {
                    if(visualisers.contains(player.getUniqueId())) {
                        Visualise.render(player, settlements.getSettlementManager());
                    }
                }), 0L, 20L
        );
    }

    public boolean isVisualiser(Player player) {
        return visualisers.contains(player.getUniqueId());
    }

    public void add(Player player ) {
        visualisers.add(player.getUniqueId());
    }

    public void remove(Player player) {
        visualisers.remove(player.getUniqueId());
    }
}
