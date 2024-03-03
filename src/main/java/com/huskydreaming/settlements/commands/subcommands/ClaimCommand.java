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

    public ClaimCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        claimService = plugin.provide(ClaimService.class);
        dependencyService = plugin.provide(DependencyService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
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
        Role role = roleService.getRole(member);

        if(role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            Collection<Chunk> chunks = claimService.getChunks(member.getSettlement());
            if(chunks.size() >= settlement.getMaxLand()) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED_MAX, settlement.getMaxLand()));
                return;
            }

            boolean isAdjacent = false;
            for(Chunk c : claimService.getChunks(member.getSettlement())) {
                if (Remote.areAdjacentChunks(chunk, c)) isAdjacent = true;
            }

            if(isAdjacent) {
                String x = String.valueOf(chunk.getX());
                String z = String.valueOf(chunk.getZ());

                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, x, z));
                claimService.setClaim(chunk, member.getSettlement());
                borderService.addPlayer(player, member.getSettlement(), Color.AQUA);
            } else {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_ADJACENT));
            }
        } else {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
        }
    }
}
