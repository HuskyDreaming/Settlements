package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Set;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements SubCommand {

    private final BorderService borderService;

    private final ChunkService chunkService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        chunkService = plugin.provide(ChunkService.class);
        dependencyService = plugin.provide(DependencyService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        if (chunkService.isDisabledWorld(player.getWorld())) {
            player.sendMessage(Locale.SETTLEMENT_LAND_DISABLED_WORLD.prefix());
            return;
        }

        if (dependencyService.isTowny(player)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_TOWNY.prefix());
            return;
        }

        if (dependencyService.isWorldGuard(player)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_WORLDGUARD.prefix());
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        if (chunkService.isClaim(chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if (role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            Set<ChunkData> chunks = chunkService.getClaims(member.getSettlement());
            if (chunks.size() >= settlement.getMaxLand()) {
                player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED_MAX.prefix(settlement.getMaxLand()));
                return;
            }

            boolean isAdjacent = false;
            for (ChunkData data : chunks) {
                if (Util.areAdjacentChunks(chunk, data.toChunk())) isAdjacent = true;
            }

            if (isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(x, z));
                chunkService.setClaim(chunk, member.getSettlement());
                borderService.addPlayer(player, member.getSettlement(), Color.AQUA);
            } else {
                player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT.prefix());
            }
        } else {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.LAND_CLAIM));
        }
    }
}