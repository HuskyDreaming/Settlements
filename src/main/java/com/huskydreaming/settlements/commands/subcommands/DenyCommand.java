package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InvitationService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Remote;
import com.huskydreaming.settlements.utilities.Locale;
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

            String string = strings[1];
            if (invitationService.hasNoInvitation(player, string)) {
                player.sendMessage(Remote.prefix(Locale.INVITATION_NULL, string));
                return;
            }

            Settlement settlement = settlementService.getSettlement(string);
            if (settlement == null) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NULL, string));
                return;
            }

            invitationService.removeInvitation(player, strings[1]);
            player.sendMessage(Remote.prefix(Locale.INVITATION_DENIED, string));
        }
    }
}
