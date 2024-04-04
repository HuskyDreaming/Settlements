package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DENY, arguments = " [settlement]")
public class DenyCommand implements SubCommand {

    private final InvitationService invitationService;
    private final SettlementService settlementService;

    public DenyCommand(HuskyPlugin plugin) {
        invitationService = plugin.provide(InvitationService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {

            String string = strings[1];
            if (invitationService.hasNoInvitation(player, string)) {
                player.sendMessage(Locale.INVITATION_NULL.prefix(string));
                return;
            }

            Settlement settlement = settlementService.getSettlement(string);
            if (settlement == null) {
                player.sendMessage(Locale.SETTLEMENT_NULL.prefix(string));
                return;
            }

            invitationService.removeInvitation(player, strings[1]);
            player.sendMessage(Locale.INVITATION_DENIED.prefix(string));
        }
    }
}