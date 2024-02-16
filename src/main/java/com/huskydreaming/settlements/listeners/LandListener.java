package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class LandListener implements Listener {

    private final MemberService memberService;

    private final ClaimService claimService;
    private final RoleService roleService;
    private final BorderService borderService;

    private final SettlementService settlementService;

    public LandListener() {
        memberService = ServiceProvider.Provide(MemberService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        borderService = ServiceProvider.Provide(BorderService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {
            String fromChunk = claimService.getClaim(event.getFrom().getChunk());
            String toChunk = claimService.getClaim(event.getTo().getChunk());

            Player player = event.getPlayer();
            if (fromChunk == null && toChunk != null) {
                Settlement settlement = settlementService.getSettlement(toChunk);
                if(settlement == null) return;

                Color color = Color.RED;
                ChatColor chatColor = ChatColor.RED;

                if(memberService.hasSettlement(player)) {
                    Member member = memberService.getCitizen(player);

                    color = member.getSettlement().equalsIgnoreCase(toChunk) ? Color.AQUA : Color.RED;
                    chatColor = member.getSettlement().equalsIgnoreCase(toChunk) ? ChatColor.AQUA : ChatColor.RED;
                }

                borderService.addPlayer(player, toChunk, color);

                player.sendTitle(
                        chatColor + "" + ChatColor.BOLD + toChunk,
                        settlement.getDescription(),
                        20, 40, 20
                );
                return;
            }

            if (toChunk == null && fromChunk != null) {
                borderService.removePlayer(player);

                player.sendTitle(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "Wilderness",
                        ChatColor.GRAY + "You have entered the wilderness.",
                        20, 40, 20
                );
            }

            if (toChunk != null && !fromChunk.equals(toChunk)) {
                borderService.addPlayer(player, toChunk, Color.RED);

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
        if(event.getAction() != Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block != null) {
                Chunk chunk = block.getChunk();
                Player player = event.getPlayer();
                event.setCancelled(isCancelled(chunk, player, RolePermission.LAND_INTERACT));
            }
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

        Member member = memberService.getCitizen(player);
        String memberSettlement = member.getSettlement();
        String currentSettlement = claimService.getClaim(chunk);

        if (memberSettlement.equalsIgnoreCase(currentSettlement)) {
            Settlement settlement = settlementService.getSettlement(memberSettlement);
            Role role = roleService.getRole(settlement, member);
            return !role.hasPermission(rolePermission) && !settlement.isOwner(player);
        }
        return true;
    }
}
