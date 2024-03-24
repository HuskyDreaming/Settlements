package com.huskydreaming.settlements.listeners;

import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
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

import java.util.Set;

public class LandListener implements Listener {

    private final BorderService borderService;
    private final ChunkService chunkService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public LandListener(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        chunkService = plugin.provide(ChunkService.class);
        dependencyService = plugin.provide(DependencyService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() != null && !event.getTo().getChunk().equals(event.getFrom().getChunk())) {

            String fromChunk = chunkService.getClaim(event.getFrom().getChunk());
            String toChunk = chunkService.getClaim(event.getTo().getChunk());

            Player player = event.getPlayer();
            if (fromChunk == null && toChunk != null) {
                handleChunkChange(player, toChunk);
                return;
            }

            if (toChunk != null && !fromChunk.equalsIgnoreCase(toChunk)) {
                handleChunkChange(player, toChunk);
                return;
            }

            if (toChunk == null && fromChunk != null) {
                if (hasAutoClaimedChunk(player, event.getTo().getChunk())) return;

                handleWildernessChange(player);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
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

    private void handleChunkChange(Player player, String toChunk) {
        Settlement settlement = settlementService.getSettlement(toChunk);
        if (settlement == null) return;

        Color color = Color.RED;
        ChatColor chatColor = ChatColor.RED;

        if (memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);

            color = member.getSettlement().equalsIgnoreCase(toChunk) ? Color.AQUA : Color.RED;
            chatColor = member.getSettlement().equalsIgnoreCase(toChunk) ? ChatColor.AQUA : ChatColor.RED;
        }

        borderService.removePlayer(player);
        borderService.addPlayer(player, toChunk, color);

        player.sendTitle(
                chatColor + Locale.SETTLEMENT_TITLE_HEADER.parameterize(Util.capitalize(toChunk)),
                Locale.SETTLEMENT_TITLE_FOOTER.parameterize(settlement.getDescription()),
                20, 40, 20
        );
    }

    private boolean hasAutoClaimedChunk(Player player, Chunk chunk) {
        if (!memberService.hasSettlement(player)) return false;

        Member member = memberService.getCitizen(player);
        if (!member.hasAutoClaim()) return false;

        if (chunkService.isDisabledWorld(player.getWorld())) {
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
        Set<ChunkData> chunks = chunkService.getClaims(member.getSettlement());
        if (chunks.size() >= settlement.getMaxLand()) {
            player.sendMessage(Locale.SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND.prefix());
            member.setAutoClaim(false);
            return false;
        }
        chunkService.setClaim(chunk, member.getSettlement());

        borderService.removePlayer(player);
        borderService.addPlayer(player, member.getSettlement(), Color.AQUA);

        player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(chunk.getX(), chunk.getZ()));
        return true;
    }

    private void handleWildernessChange(Player player) {
        borderService.removePlayer(player);

        player.sendTitle(
                Locale.WILDERNESS_TITLE.parse(),
                Locale.WILDERNESS_FOOTER.parse(),
                20, 40, 20
        );
    }

    private boolean isCancelled(Chunk chunk, Player player, RolePermission rolePermission) {
        if (!chunkService.isClaim(chunk)) return false;
        if (memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            String memberSettlement = member.getSettlement();
            String currentSettlement = chunkService.getClaim(chunk);

            if (memberSettlement.equalsIgnoreCase(currentSettlement)) {
                Settlement settlement = settlementService.getSettlement(memberSettlement);
                Role role = roleService.getRole(member);
                return !role.hasPermission(rolePermission) && !settlement.isOwner(player);
            }
        }
        return true;
    }
}