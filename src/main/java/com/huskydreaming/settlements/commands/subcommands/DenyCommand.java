package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InvitationService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.Chat;
import com.huskydreaming.settlements.utilities.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DENY)
public class DenyCommand implements CommandInterface {

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            InvitationService invitationService = (InvitationService) ServiceRegistry.getService(ServiceType.INVITATION);

            String string = strings[1];
            if (invitationService.hasNoInvitation(player, string)) {
                player.sendMessage(Chat.parameterize(Locale.INVITATION_NULL, string));
                return;
            }

            SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
            Settlement settlement = settlementService.getSettlement(string);

            if (settlement == null) {
                player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_NULL, string));
                return;
            }

            invitationService.removeInvitation(player, strings[1]);
            player.sendMessage(Chat.parameterize(Locale.INVITATION_DENIED, string));
        }
    }
}
