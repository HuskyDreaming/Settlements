package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements CommandInterface {

    @Inject
    private CitizenService citizenService;

    @Inject
    private ClaimService claimService;

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if(!citizenService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Citizen citizen = citizenService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(citizen.getSettlement());

        Chunk chunk = player.getLocation().getChunk();
        if(claimService.isClaim(chunk)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED));
            return;
        }

        Role role = settlement.getRole(citizen.getRole());
        player.sendMessage(role.getName());

        if(role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            String x = String.valueOf(chunk.getX());
            String z = String.valueOf(chunk.getZ());

            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIM, x, z));
            claimService.setClaim(settlement.getName(), chunk);
        } else {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
        }
    }
}
