package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
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

    private final Settlements settlements;

    public LandListener(Settlements settlements) {
        this.settlements = settlements;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {
            SettlementManager settlementManager = settlements.getSettlementManager();
            Chunk from = event.getFrom().getChunk();
            Chunk to = event.getTo().getChunk();

            Settlement fromSettlement = settlementManager.getSettlement(from);
            Settlement toSettlement = settlementManager.getSettlement(to);

            Player player = event.getPlayer();
            if (fromSettlement == null && toSettlement != null) {
                player.sendTitle(
                        ChatColor.AQUA + "" + ChatColor.BOLD + toSettlement.getName(),
                        ChatColor.GRAY + toSettlement.getDescription(),
                        20, 30, 20
                );
                return;
            }

            if (toSettlement == null && fromSettlement != null) {
                player.sendTitle(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "Wilderness",
                        ChatColor.GRAY + "You have entered the wilderness.",
                        20, 30, 20
                );
            }

            if (toSettlement != null && toSettlement != fromSettlement) {
                player.sendTitle(
                        ChatColor.RED + "" + ChatColor.BOLD + toSettlement.getName(),
                        ChatColor.GRAY + toSettlement.getDescription(),
                        20, 30, 20
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
        if (!settlements.getSettlementManager().isClaimed(chunk)) return false;

        Settlement settlement = settlements.getSettlementManager().getSettlement(chunk);
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
