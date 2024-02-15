package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BorderServiceImpl implements BorderService {

    private final ClaimService claimService;
    private final SettlementService settlementService;
    private final Map<UUID, Settlement> borders = new ConcurrentHashMap<>();

    public BorderServiceImpl() {
        claimService = ServiceProvider.Provide(ClaimService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void addPlayer(Player player, String settlementName) {
        Settlement settlement = settlementService.getSettlement(settlementName);
        if(settlement != null) borders.put(player.getUniqueId(), settlement);
    }

    @Override
    public void removePlayer(Player player) {
        borders.remove(player.getUniqueId());
    }

    @Override
    public void run(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                borders.forEach((uuid, settlement) -> Remote.render(claimService, settlement));
            }
        }.runTaskTimer(plugin, 20L, 0L);
    }
}
