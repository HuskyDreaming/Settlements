package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Visualise;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class LandListener implements Listener {

    private final SettlementService settlementService;

    public LandListener(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {
            Chunk from = event.getFrom().getChunk();
            Chunk to = event.getTo().getChunk();

            Settlement fromSettlement = settlementService.getSettlement(from);
            Settlement toSettlement = settlementService.getSettlement(to);

            Player player = event.getPlayer();
            if (fromSettlement == null && toSettlement != null) {
                Visualise.render(player, to, Color.AQUA);
                player.sendTitle(
                        ChatColor.AQUA + "" + ChatColor.BOLD + toSettlement.getName(),
                        ChatColor.GRAY + toSettlement.getDescription(),
                        20, 40, 20
                );
                return;
            }

            if (toSettlement == null && fromSettlement != null) {
                Visualise.render(player, to, Color.LIME);
                player.sendTitle(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "Wilderness",
                        ChatColor.GRAY + "You have entered the wilderness.",
                        20, 40, 20
                );
            }

            if (toSettlement != null && toSettlement != fromSettlement) {
                Visualise.render(player, to, Color.RED);
                player.sendTitle(
                        ChatColor.RED + "" + ChatColor.BOLD + toSettlement.getName(),
                        ChatColor.GRAY + toSettlement.getDescription(),
                        20, 40, 20
                );
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            Chunk chunk = block.getChunk();
            Player player = event.getPlayer();
            event.setCancelled(isCancelled(chunk, player, RolePermission.LAND_INTERACT));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, RolePermission.LAND_BREAK));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, RolePermission.LAND_PLACE));
    }

    private boolean isCancelled(Chunk chunk, Player player, RolePermission rolePermission) {
        if (!settlementService.isSettlement(chunk)) return false;

        Settlement settlement = settlementService.getSettlement(chunk);
        if (settlement == null) return false;

        boolean cancel = true;
        if (settlement.isCitizen(player)) {
            Role role = settlement.getRole(player);
            if (role.hasPermission(rolePermission) || settlement.isOwner(player)) {
                cancel = false;
            }
        }
        return cancel;
    }
}
