package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.transience.BorderData;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BorderServiceImpl implements BorderService {

    private final ClaimService claimService;
    private final SettlementService settlementService;
    private final Map<Player, List<BorderData>> borders = new ConcurrentHashMap<>();

    public BorderServiceImpl() {
        claimService = ServiceProvider.Provide(ClaimService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void addPlayer(Player player, String settlementName, Color color) {
        Settlement settlement = settlementService.getSettlement(settlementName);
        if(settlement != null) borders.put(player, Remote.calculatePositions(claimService, settlement, color));
    }

    @Override
    public void removePlayer(Player player) {
        borders.remove(player);
    }

    @Override
    public void run(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Player, List<BorderData>> entry : borders.entrySet()) {
                    Player player = entry.getKey();

                    for (BorderData data : entry.getValue()) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(data.color(), 1.0f);
                        claimService.getClaim(player.getLocation().getChunk());
                        player.spawnParticle(Particle.REDSTONE, data.x(), data.y(), data.z(), 1, dustOptions);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
}
