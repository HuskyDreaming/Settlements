package com.huskydreaming.settlements.listeners;

import com.google.inject.Inject;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Remote;
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

    @Inject
    private CitizenService citizenService;

    @Inject
    private ClaimService claimService;

    @Inject
    private SettlementService settlementService;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {
            String fromChunk = claimService.getClaim(event.getFrom().getChunk());
            String toChunk = claimService.getClaim(event.getTo().getChunk());

            Player player = event.getPlayer();
            if (fromChunk == null && toChunk != null) {
                Remote.render(player, event.getFrom().getChunk(), Color.AQUA);
                player.sendTitle(
                        ChatColor.AQUA + "" + ChatColor.BOLD + toChunk,
                        "Welcome to the settlement",
                        20, 40, 20
                );
                return;
            }

            if (toChunk == null && fromChunk != null) {
                Remote.render(player, event.getFrom().getChunk(), Color.GREEN);
                player.sendTitle(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "Wilderness",
                        ChatColor.GRAY + "You have entered the wilderness.",
                        20, 40, 20
                );
            }

            if (toChunk != null && !toChunk.equals(fromChunk)) {
                Remote.render(player, event.getFrom().getChunk(), Color.RED);
                player.sendTitle(
                        ChatColor.RED + "" + ChatColor.BOLD + toChunk,
                        ChatColor.GRAY + "Welcome to the settlement",
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
        if (!claimService.isClaim(chunk)) return false;
        if( citizenService.hasSettlement(player)) return true;

        Citizen citizen = citizenService.getCitizen(player);

        boolean cancel = true;
        if (claimService.getClaim(chunk).equalsIgnoreCase(citizen.getSettlement())) {
            Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
            Role role = settlement.getRole(citizen.getRole());
            if (role.hasPermission(rolePermission) || settlement.isOwner(player)) {
                cancel = false;
            }
        }
        return cancel;
    }
}
