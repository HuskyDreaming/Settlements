package com.huskydreaming.settlements.listeners;

import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;
import java.util.List;

public class LandListener implements Listener {

    private final BorderService borderService;
    private final ConfigService configService;
    private final ClaimService claimService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final NotificationService notificationService;
    private final SettlementService settlementService;
    private final TrustService trustService;

    public LandListener(SettlementPlugin plugin) {

        borderService = plugin.provide(BorderService.class);
        configService = plugin.provide(ConfigService.class);
        claimService = plugin.provide(ClaimService.class);
        dependencyService = plugin.provide(DependencyService.class);
        memberService = plugin.provide(MemberService.class);
        notificationService = plugin.provide(NotificationService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
        trustService = plugin.provide(TrustService.class);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getTo() != null && !event.getTo().getChunk().equals(event.getFrom().getChunk())) {
            sendChunkChange(event.getFrom().getChunk(), event.getTo().getChunk(), event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() != null && !event.getTo().getChunk().equals(event.getFrom().getChunk())) {
            sendChunkChange(event.getFrom().getChunk(), event.getTo().getChunk(), event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block != null) {
                Chunk chunk = block.getChunk();
                Player player = event.getPlayer();
                event.setCancelled(isCancelled(chunk, player, RolePermission.CLAIM_INTERACT));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, RolePermission.CLAIM_BREAK));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, RolePermission.CLAIM_PLACE));
    }

    private void handleChunkChange(Player player, String toChunk) {
        Settlement settlement = settlementService.getSettlement(toChunk);
        if (settlement == null) return;

        if(trustService.hasTrusts(player)) {
            List<OfflinePlayer> offlinePlayers = trustService.getOfflinePlayers(toChunk);
            if(offlinePlayers.contains(player)) {
                borderService.removePlayer(player);
                borderService.addPlayer(player, toChunk, Color.YELLOW);
                notificationService.sendTrust(player, toChunk, settlement.getDescription());
                return;
            }
        }

        boolean isClaim = false;
        if (memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            isClaim = member.getSettlement().equalsIgnoreCase(toChunk);
        }

        borderService.removePlayer(player);
        borderService.addPlayer(player, toChunk, isClaim ? Color.AQUA : Color.RED);
        notificationService.sendSettlement(player, toChunk, settlement.getDescription(), isClaim);
    }

    private boolean hasAutoClaimedChunk(Player player, Chunk chunk) {
        if (!memberService.hasSettlement(player)) return false;

        Member member = memberService.getCitizen(player);
        if (!member.hasAutoClaim()) return false;

        Config config = configService.getConfig();
        if (config.containsDisableWorld(player.getWorld())) {
            player.sendMessage(Locale.SETTLEMENT_LAND_DISABLED_WORLD.prefix());
            return false;
        }

        if (dependencyService.isTowny(player)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_TOWNY.prefix());
            return false;
        }

        if (dependencyService.isWorldGuard(player)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_WORLDGUARD.prefix());
            return false;
        }

        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Set<ChunkData> chunks = claimService.getClaims(member.getSettlement());
        if (chunks.size() >= settlement.getMaxLand()) {
            player.sendMessage(Locale.SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND.prefix());
            member.setAutoClaim(false);
            return false;
        }
        claimService.setClaim(chunk, member.getSettlement());

        borderService.removePlayer(player);
        borderService.addPlayer(player, member.getSettlement(), Color.AQUA);

        player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(chunk.getX(), chunk.getZ()));
        return true;
    }

    private boolean isCancelled(Block block, Player player) {
        Chunk chunk = block.getChunk();
        if (!claimService.isClaim(chunk)) return false;
        if (dependencyService.isTowny(player, block)) return false;
        return !dependencyService.isWorldGuard(block);
    }

    private void sendChunkChange(Chunk from, Chunk to, Player player) {
        String fromChunk = claimService.getClaim(from);
        String toChunk = claimService.getClaim(to);

        if (fromChunk == null && toChunk != null) {
            handleChunkChange(player, toChunk);
            return;
        }

        if (toChunk != null && !fromChunk.equalsIgnoreCase(toChunk)) {
            handleChunkChange(player, toChunk);
            return;
        }

        if (toChunk == null && fromChunk != null) {
            if (hasAutoClaimedChunk(player, to)) return;
            borderService.removePlayer(player);
            notificationService.sendWilderness(player);
        }
    }


    private boolean isCancelled(Chunk chunk, Player player, RolePermission rolePermission) {
        if (!claimService.isClaim(chunk)) return false;

        if (memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            String memberSettlement = member.getSettlement();
            String currentSettlement = claimService.getClaim(chunk);

            if (memberSettlement.equalsIgnoreCase(currentSettlement)) {
                Settlement settlement = settlementService.getSettlement(memberSettlement);
                Role role = roleService.getRole(member);

                if (dependencyService.isWorldGuard(player)) return true;
                if (dependencyService.isTowny(player)) return true;

                return !role.hasPermission(rolePermission) && !settlement.isOwner(player);
            }
        }
        return true;
    }
}