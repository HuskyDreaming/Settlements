package com.huskydreaming.settlements.commands.subcommands;

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

@Command(label = CommandLabel.UNCLAIM)
public class UnclaimCommand implements CommandInterface {

    private final CitizenService citizenService;
    private final ClaimService claimService;
    private final SettlementService settlementService;

    public UnclaimCommand(CitizenService citizenService, SettlementService settlementService, ClaimService claimService) {
        this.citizenService = citizenService;
        this.settlementService = settlementService;
        this.claimService = claimService;
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!citizenService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Citizen citizen = citizenService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
        Role role = settlement.getRole(citizen.getRole());
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

        if(settlement.getName().equalsIgnoreCase(claim)) {
            claimService.removeClaim(chunk);
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_UNCLAIM));
            return;
        }

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LAND_CLAIMED));
    }
}
