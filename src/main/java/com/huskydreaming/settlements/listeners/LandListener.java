package com.huskydreaming.settlements.listeners;

import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
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

import java.util.Collection;
import java.util.Objects;

public class LandListener implements Listener {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public LandListener() {
        borderService = ServiceProvider.Provide(BorderService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        dependencyService = ServiceProvider.Provide(DependencyService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (Objects.requireNonNull(event.getTo()).getChunk() != event.getFrom().getChunk()) {

            String fromChunk = claimService.getClaim(event.getFrom().getChunk());
            String toChunk = claimService.getClaim(event.getTo().getChunk());

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
                if(hasAutoClaimedChunk(player, event.getTo().getChunk())) return;

                handleWildernessChange(player);
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
                chatColor + Remote.parameterize(Locale.SETTLEMENT_TITLE_HEADER, toChunk),
                Remote.parameterize(Locale.SETTLEMENT_TITLE_FOOTER, settlement.getDescription()),
                20, 40, 20
        );
    }

    private boolean hasAutoClaimedChunk(Player player, Chunk chunk) {
        if (!memberService.hasSettlement(player)) return false;

        Member member = memberService.getCitizen(player);
        if (!member.hasAutoClaim()) return false;

        if(claimService.isDisabledWorld(player.getWorld())) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_DISABLED_WORLD));
            return false;
        }
        if(dependencyService.isWorldGuard(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_WORLDGUARD));
            return false;
        }

        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Collection<Chunk> chunks = claimService.getChunks(settlement);
        if (chunks.size() >= settlement.getMaxLand()) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_AUTO_CLAIM_OFF_MAX_LAND));
            member.setAutoClaim(false);
            return false;
        }
        claimService.setClaim(chunk, settlement);

        borderService.removePlayer(player);
        borderService.addPlayer(player, member.getSettlement(), Color.AQUA);

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, chunk.getX(), chunk.getZ()));
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
        if (!claimService.isClaim(chunk)) return false;
        if(memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            String memberSettlement = member.getSettlement();
            String currentSettlement = claimService.getClaim(chunk);

            if (memberSettlement.equalsIgnoreCase(currentSettlement)) {
                Settlement settlement = settlementService.getSettlement(memberSettlement);
                Role role = roleService.getRole(settlement, member);
                return !role.hasPermission(rolePermission) && !settlement.isOwner(player);
            }
        }
        return true;
    }
}
