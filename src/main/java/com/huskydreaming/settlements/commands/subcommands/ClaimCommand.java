package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements CommandInterface {

    private final MemberService memberService;
    private final ClaimService claimService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ClaimCommand() {
        memberService = ServiceProvider.Provide(MemberService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if(!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(claimService.isClaim(chunk)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED));
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(settlement, member);

        if(role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            boolean isAdjacent = false;
            for(Chunk c : claimService.getChunks(settlement)) {
                if (Remote.areAdjacentChunks(chunk, c)) isAdjacent = true;
            }

            if(isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, x, z));
                claimService.setClaim(chunk, settlement);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_ADJACENT));
            }
        } else {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
        }
    }
}
