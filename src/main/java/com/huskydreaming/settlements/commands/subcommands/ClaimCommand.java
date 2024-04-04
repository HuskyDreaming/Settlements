package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Config;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Set;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements SubCommand {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
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

        Config config = configService.getConfig();
        if (config.containsDisableWorld(player.getWorld())) {
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
        if (claimService.isClaim(chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if (role.hasPermission(RolePermission.CLAIM_LAND) || settlement.isOwner(player)) {
            Set<ChunkData> chunks = claimService.getClaims(member.getSettlement());
            if (chunks.size() >= settlement.getMaxLand()) {
                player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED_MAX.prefix(settlement.getMaxLand()));
                return;
            }

            if (claimService.isAdjacentToOtherClaim(member.getSettlement(), chunk)) {
                player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT_OTHER.prefix());
                return;
            }

            if (claimService.isAdjacent(member.getSettlement(), chunk)) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(x, z));
                claimService.setClaim(chunk, member.getSettlement());
                borderService.addPlayer(player, member.getSettlement(), Color.AQUA);
            } else {
                player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT.prefix());
            }
        } else {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.CLAIM_LAND));
        }
    }
}