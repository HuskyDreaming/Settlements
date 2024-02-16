package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.UNCLAIM)
public class UnclaimCommand implements CommandInterface {

    private final BorderService borderService;
    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;

    private final SettlementService settlementService;

    public UnclaimCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(settlement, member);
        if (!(role.hasPermission(RolePermission.LAND_UNCLAIM) || settlement.isOwner(player))) {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS), RolePermission.LAND_UNCLAIM.getName());
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        String claim = claimService.getClaim(chunk);
        if(claim == null) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_NOT_CLAIMED));
            return;
        }

        if(claimService.getChunks(settlement).size() == 1) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_UNCLAIM_ONE));
            return;
        }

        if(settlement.getName().equalsIgnoreCase(claim)) {
            claimService.removeClaim(chunk);
            borderService.removePlayer(player);
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_UNCLAIM));
            return;
        }

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED));
    }
}
