package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Claim;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAnnotation(label = CommandLabel.UN_CLAIM)
public class UnClaimCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final MemberService memberService;
    private final PermissionService permissionService;

    private final RoleService roleService;

    private final SettlementService settlementService;

    public UnClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        configService = plugin.provide(ConfigService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        if(configService.isDisabledWorld(player)) return;

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        Claim claim = claimService.getClaim(chunk);
        if (claim == null) {
            player.sendMessage(Message.LAND_NOT_CLAIMED.prefix());
            return;
        }


        if (claimService.getClaims(settlement).size() == 1) {
            player.sendMessage(Message.LAND_UN_CLAIM_ONE.prefix());
            return;
        }

        if (claim.getSettlementId() == member.getSettlementId()) {
            claimService.removeClaim(claim, () -> {
                borderService.removePlayer(player);
                player.sendMessage(Message.LAND_UN_CLAIM.prefix());
            });
            return;
        }

        player.sendMessage(Message.LAND_CLAIMED.prefix());
    }
}