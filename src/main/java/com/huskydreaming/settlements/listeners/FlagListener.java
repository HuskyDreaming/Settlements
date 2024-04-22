package com.huskydreaming.settlements.listeners;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.settlements.enumeration.Flag;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.FlagService;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Redstone;

public class FlagListener implements Listener {

    private final ClaimService claimService;
    private final FlagService flagService;

    public FlagListener(HuskyPlugin plugin) {
        this.claimService = plugin.provide(ClaimService.class);
        this.flagService = plugin.provide(FlagService.class);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Chunk chunk = event.getLocation().getChunk();
        if (event.getEntity() instanceof Animals && claimService.isClaim(chunk)) {
            String settlement = claimService.getClaim(chunk);
            event.setCancelled(!flagService.hasFlag(settlement, Flag.ANIMAL_SPAWNING));
        }

        if (event.getEntity() instanceof Monster && claimService.isClaim(chunk)) {
            String settlement = claimService.getClaim(chunk);
            event.setCancelled(!flagService.hasFlag(settlement, Flag.MONSTER_SPAWNING));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Chunk chunk = event.getFrom().getChunk();
            if (claimService.isClaim(chunk)) {
                String settlement = claimService.getClaim(chunk);
                event.setCancelled(!flagService.hasFlag(settlement, Flag.END_PORTAL));
                return;
            }
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && event.getTo() != null) {
            Chunk chunk = event.getTo().getChunk();
            if (claimService.isClaim(chunk)) {
                String settlement = claimService.getClaim(chunk);
                event.setCancelled(!flagService.hasFlag(settlement, Flag.ENDER_PEARL));
            }
        }
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player player) {
            Chunk chunk = player.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                String settlement = claimService.getClaim(chunk);
                event.setCancelled(!flagService.hasFlag(settlement, Flag.PVP));
            }
        }
    }

    @EventHandler
    public void onAnimalDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Animals animals) {
            Chunk chunk = animals.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                String settlement = claimService.getClaim(chunk);
                event.setCancelled(!flagService.hasFlag(settlement, Flag.ANIMAL_KILLING));
            }
        }
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
        Chunk chunk = event.getLocation().getChunk();
        if (claimService.isClaim(chunk)) {
            String settlement = claimService.getClaim(chunk);
            event.setCancelled(!flagService.hasFlag(settlement, Flag.EXPLOSIONS));
            return;
        }

        for (Block block : event.blockList()) {
            if (claimService.isClaim(block.getChunk())) {
                String settlement = claimService.getClaim(block.getChunk());
                event.setCancelled(!flagService.hasFlag(settlement, Flag.EXPLOSIONS));
                break;
            }
        }
    }

    @EventHandler
    public void onLavaSpread(BlockFromToEvent event) {
        if (event.getToBlock().getType() == Material.LAVA) {
            if (claimService.isClaim(event.getToBlock().getChunk())) {
                String settlement = claimService.getClaim(event.getBlock().getChunk());
                event.setCancelled(!flagService.hasFlag(settlement, Flag.LAVA_SPREAD));
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (claimService.isClaim(event.getBlock().getChunk())) {
            String settlement = claimService.getClaim(event.getBlock().getChunk());
            event.setCancelled(!flagService.hasFlag(settlement, Flag.EXPLOSIONS));
            return;
        }

        for (Block block : event.blockList()) {
            if (claimService.isClaim(block.getChunk())) {
                String settlement = claimService.getClaim(block.getChunk());
                event.setCancelled(!flagService.hasFlag(settlement, Flag.EXPLOSIONS));
                break;
            }
        }
    }

    @EventHandler
    public void onChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Monster) {
            if (claimService.isClaim(event.getBlock().getChunk())) {
                String settlement = claimService.getClaim(event.getBlock().getChunk());
                event.setCancelled(!flagService.hasFlag(settlement, Flag.LAVA_SPREAD));
            }
        }
    }

    @EventHandler
    public void onLavaSpread(BlockSpreadEvent event) {
        if (event.getBlock().getType() == Material.LAVA) {
            if (claimService.isClaim(event.getBlock().getChunk())) {
                String settlement = claimService.getClaim(event.getBlock().getChunk());
                event.setCancelled(!flagService.hasFlag(settlement, Flag.LAVA_SPREAD));
            }
        }
    }
}