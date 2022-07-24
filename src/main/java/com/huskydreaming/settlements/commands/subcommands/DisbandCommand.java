package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DISBAND)
public class DisbandCommand implements CommandInterface {

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
        if(!settlement.isOwner(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_OWNER));
            return;
        }

        claimService.clean(settlement);
        settlementService.disbandSettlement(settlement);

        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_DISBAND));
    }
}
