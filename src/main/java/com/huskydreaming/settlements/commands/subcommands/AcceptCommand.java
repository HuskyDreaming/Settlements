package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.InvitationManager;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.entity.Player;

public class AcceptCommand extends CommandBase {

    public AcceptCommand() {
        super("accept");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        if (strings.length == 2) {
            InvitationManager invitationManager = settlements.getInvitationManager();
            if (!invitationManager.hasInvitation(player, strings[1])) {
                player.sendMessage("You do not have an invitation for " + strings[1] + ".");
                return;
            }

            Settlement settlement = settlements.getSettlementManager().getSettlement(strings[1]);
            if (settlement == null) {
                player.sendMessage("The settlement " + strings[1] + " does not seem to exist.");
                return;
            }

            player.sendMessage("You have joined the settlement.");
            invitationManager.removeInvitation(player, strings[0]);
            settlement.add(player);
        }
    }
}
