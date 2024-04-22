package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.UN_CLAIM)
public class UnClaimCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final ConfigService configService;
    private final RoleService roleService;

    private final SettlementService settlementService;

    public UnClaimCommand(HuskyPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        configService = plugin.provide(ConfigService.class);
        memberService = plugin.provide(MemberService.class);
        claimService = plugin.provide(ClaimService.class);
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

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if(!(role.hasPermission(RolePermission.CLAIM_LAND) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        String claim = claimService.getClaim(chunk);
        if (claim == null) {
            player.sendMessage(Message.LAND_NOT_CLAIMED.prefix());
            return;
        }


        if (claimService.getClaims(member.getSettlement()).size() == 1) {
            player.sendMessage(Message.LAND_UN_CLAIM_ONE.prefix());
            return;
        }

        if (member.getSettlement().equalsIgnoreCase(claim)) {
            claimService.removeClaim(chunk);
            borderService.removePlayer(player);
            player.sendMessage(Message.LAND_UN_CLAIM.prefix());
            return;
        }

        player.sendMessage(Message.LAND_CLAIMED.prefix());
    }
}