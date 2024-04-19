package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.types.SettlementDefaultType;
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

import java.util.List;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.CLAIM)
public class ClaimCommand implements PlayerCommandProvider {

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
    public void onCommand(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        if (configService.isDisabledWorld(player)) return;
        if (dependencyService.isDependency(player)) return;

        Chunk chunk = player.getLocation().getChunk();
        if (claimService.isClaim(chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if(!(role.hasPermission(RolePermission.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix());
            return;
        }

        Set<ChunkData> chunks = claimService.getClaims(member.getSettlement());
        Config config = configService.getConfig();
        if (chunks.size() >= config.getSettlementDefault(SettlementDefaultType.MAX_CLAIMS)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED_MAX.prefix(settlement.getMaxLand()));
            return;
        }

        if (claimService.isAdjacentToOtherClaim(member.getSettlement(), chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT_OTHER.prefix());
            return;
        }

        if (!claimService.isAdjacent(member.getSettlement(), chunk)) {
            player.sendMessage(Locale.SETTLEMENT_LAND_ADJACENT.prefix());
            return;
        }

        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());

        player.sendMessage(Locale.SETTLEMENT_LAND_CLAIM.prefix(x, z));
        claimService.setClaim(chunk, member.getSettlement());
        borderService.addPlayer(player, member.getSettlement(), Color.AQUA);
    }
}