package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.UNCLAIM)
public class UnclaimCommand implements CommandInterface {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;

    private final SettlementService settlementService;

    public UnclaimCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        claimService = plugin.provide(ClaimService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);
        if (!(role.hasPermission(RolePermission.LAND_UNCLAIM) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.LAND_UNCLAIM.getName()));
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        String claim = claimService.getClaim(chunk);
        if (claim == null) {
            player.sendMessage(Locale.SETTLEMENT_LAND_NOT_CLAIMED.prefix());
            return;
        }


        if (claimService.getClaims(member.getSettlement()).size() == 1) {
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM_ONE.prefix());
            return;
        }

        if (member.getSettlement().equalsIgnoreCase(claim)) {
            claimService.removeClaim(chunk);
            borderService.removePlayer(player);
            player.sendMessage(Locale.SETTLEMENT_LAND_UNCLAIM.prefix());
            return;
        }

        player.sendMessage(Locale.SETTLEMENT_LAND_CLAIMED.prefix());
    }
}