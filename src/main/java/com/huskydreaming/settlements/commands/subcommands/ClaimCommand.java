package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAnnotation(label = CommandLabel.CLAIM)
public class ClaimCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final ContainerService containerService;
    private final DependencyService dependencyService;
    private final PermissionService permissionService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        containerService = plugin.provide(ContainerService.class);
        dependencyService = plugin.provide(DependencyService.class);
        permissionService = plugin.provide(PermissionService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }


        if (configService.isDisabledWorld(player)) return;
        if (dependencyService.isDependency(player)) return;

        Chunk chunk = player.getLocation().getChunk();
        if (claimService.isClaim(chunk)) {
            player.sendMessage(Message.LAND_CLAIMED.prefix());
            return;
        }


        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);
        Set<PermissionType> permissions = permissionService.getPermissions(role);

        if(!(permissions.contains(PermissionType.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Set<Claim> claims = claimService.getClaims(settlement);
        Container container = containerService.getContainer(settlement);
        int maxClaims = container.getMaxClaims();
        if (claims.size() >= maxClaims) {
            player.sendMessage(Message.LAND_CLAIMED_MAX.prefix(maxClaims));
            return;
        }

        if (claimService.isAdjacentToOtherClaim(settlement, chunk)) {
            player.sendMessage(Message.LAND_ADJACENT_OTHER.prefix());
            return;
        }

        if (!claimService.isAdjacent(settlement, chunk)) {
            player.sendMessage(Message.LAND_ADJACENT.prefix());
            return;
        }

        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());

        claimService.addClaim(settlement, chunk, () -> {
            borderService.addPlayer(player, settlement, Color.AQUA);
            player.sendMessage(Message.LAND_CLAIM.prefix(x, z));
        });
    }
}