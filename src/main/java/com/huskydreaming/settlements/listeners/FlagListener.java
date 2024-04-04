package com.huskydreaming.settlements.listeners;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.enumeration.Flag;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.FlagService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FlagListener implements Listener {

    private final ClaimService claimService;
    private final FlagService flagService;

    public FlagListener(HuskyPlugin plugin) {
        this.claimService = plugin.provide(ClaimService.class);
        this.flagService = plugin.provide(FlagService.class);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {

    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (claimService.isClaim(event.getBlock().getChunk())) {
            String settlement = claimService.getClaim(event.getBlock().getChunk());
            event.setCancelled(!flagService.hasFlag(settlement, Flag.LEAF_DECAY));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (claimService.isClaim(event.getLocation().getChunk())) {
            String settlement = claimService.getClaim(event.getLocation().getChunk());
            event.setCancelled(!flagService.hasFlag(settlement, Flag.EXPLOSIONS));
        }
    }
}
