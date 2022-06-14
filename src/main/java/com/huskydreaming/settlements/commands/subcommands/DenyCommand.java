package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InvitationService;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DENY)
public class DenyCommand implements CommandInterface {

    @Inject
    private InvitationService invitationService;

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (invitationService.hasNoInvitation(player, strings[1])) {
                player.sendMessage("You do not have an invitation for " + strings[1] + ".");
                return;
            }

            Settlement settlement = settlementService.getSettlement(strings[1]);
            if (settlement == null) {
                player.sendMessage("The settlement " + strings[1] + " does not seem to exist.");
                return;
            }

            invitationService.removeInvitation(player, strings[1]);
            player.sendMessage("You have denied invitation to the " + settlement.getName() + "settlement.");
        }
    }
}
