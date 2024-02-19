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
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements CommandInterface {

    private final BorderService borderService;

    private final ClaimService claimService;
    private final DependencyService dependencyService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public ClaimCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        claimService = ServiceProvider.Provide(ClaimService.class);
        dependencyService = ServiceProvider.Provide(DependencyService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if(!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        if(claimService.isDisabledWorld(player.getWorld())) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_DISABLED_WORLD));
            return;
        }

        if(dependencyService.isWorldGuard(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_WORLDGUARD));
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
            Collection<Chunk> chunks = claimService.getChunks(settlement);
            if(chunks.size() >= settlement.getMaxLand()) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED_MAX, settlement.getMaxLand()));
                return;
            }

            boolean isAdjacent = false;
            for(Chunk c : claimService.getChunks(settlement)) {
                if (Remote.areAdjacentChunks(chunk, c)) isAdjacent = true;
            }

            if(isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, x, z));
                claimService.setClaim(chunk, settlement);
                borderService.addPlayer(player, settlement.getName(), Color.AQUA);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_ADJACENT));
            }
        } else {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
        }
    }
}
