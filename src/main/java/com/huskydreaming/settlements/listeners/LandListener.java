package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.database.persistence.Config;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
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

public class LandListener implements Listener {

    private final BorderService borderService;
    private final ConfigService configService;
    private final ClaimService claimService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final PermissionService permissionService;
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
        permissionService = plugin.provide(PermissionService.class);
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
                event.setCancelled(isCancelled(chunk, player, PermissionType.CLAIM_INTERACT));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, PermissionType.CLAIM_BREAK));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        event.setCancelled(isCancelled(chunk, player, PermissionType.CLAIM_PLACE));
    }

    private void handleChunkChange(Player player, Claim claim) {
        if(trustService.hasTrusts(player)) {
            Set<OfflinePlayer> offlinePlayers = trustService.getOfflinePlayers(claim.getSettlementId());
            if(offlinePlayers.contains(player)) {
                Settlement settlement = settlementService.getSettlement(claim);
                borderService.addPlayer(player, settlement, Color.YELLOW);
                notificationService.sendTrust(player, settlement);
                return;
            }
        }

        boolean isClaim = false;
        if (memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            isClaim = (member.getSettlementId() == claim.getSettlementId());
        }

        Settlement settlement = settlementService.getSettlement(claim.getSettlementId());
        borderService.removePlayer(player);
        borderService.addPlayer(player, claim.getSettlementId(), isClaim ? Color.AQUA : Color.RED);
        notificationService.sendSettlement(player, settlement, isClaim);
    }

    private boolean hasAutoClaimedChunk(Player player, Chunk chunk) {
        if (!memberService.hasSettlement(player)) return false;

        Member member = memberService.getMember(player);
        if (!member.isAutoClaim()) return false;

        Config config = configService.getConfig();
        if (config.containsDisableWorld(player.getWorld())) {
            player.sendMessage(Message.LAND_DISABLED_WORLD.prefix());
            return false;
        }

        if (dependencyService.isTowny(player)) {
            player.sendMessage(Message.LAND_TOWNY.prefix());
            return false;
        }

        if (dependencyService.isWorldGuard(player)) {
            player.sendMessage(Message.LAND_WORLD_GUARD.prefix());
            return false;
        }

        Settlement settlement = settlementService.getSettlement(member);
        Set<Claim> claims = claimService.getClaims(settlement);
        if (claims.size() >= config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS)) {
            player.sendMessage(Message.AUTO_CLAIM_OFF_MAX_LAND.prefix());
            member.setAutoClaim(false);
            return false;
        }

        claimService.addClaim(settlement, chunk, () -> {
            borderService.removePlayer(player);
            borderService.addPlayer(player, settlement, Color.AQUA);
            player.sendMessage(Message.LAND_CLAIM.prefix(chunk.getX(), chunk.getZ()));
        });
        return true;
    }

    private boolean isCancelled(Block block, Player player) {
        Chunk chunk = block.getChunk();
        if (!claimService.isClaim(chunk)) return false;
        if (dependencyService.isTowny(player, block)) return false;
        return !dependencyService.isWorldGuard(block);
    }

    private void sendChunkChange(Chunk from, Chunk to, Player player) {
        Claim fromClaim = claimService.getClaim(from);
        Claim toClaim = claimService.getClaim(to);

        if (fromClaim == null && toClaim != null) {
            handleChunkChange(player, toClaim);
            return;
        }

        if (toClaim != null && (fromClaim.getSettlementId() != toClaim.getSettlementId())) {
            handleChunkChange(player, toClaim);
            return;
        }

        if (toClaim == null && fromClaim != null) {
            if (hasAutoClaimedChunk(player, to)) return;
            borderService.removePlayer(player);
            notificationService.sendWilderness(player);
        }
    }


    private boolean isCancelled(Chunk chunk, Player player, PermissionType permissionType) {
        if (!claimService.isClaim(chunk)) return false;

        if (memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member);
            Claim claim = claimService.getClaim(chunk);

            if (claim.getSettlementId() == member.getSettlementId()) {
                Role role = roleService.getRole(member);

                if (dependencyService.isWorldGuard(player)) return true;
                if (dependencyService.isTowny(player)) return true;

                Set<PermissionType> permissions = permissionService.getPermissions(role);
                return !permissions.contains(permissionType) && !settlement.isOwner(player);
            }
        }
        return true;
    }
}