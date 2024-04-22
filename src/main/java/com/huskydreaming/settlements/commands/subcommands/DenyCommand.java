package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.DENY, arguments = " [settlement]")
public class DenyCommand implements PlayerCommandProvider {

    private final InvitationService invitationService;
    private final SettlementService settlementService;

    public DenyCommand(HuskyPlugin plugin) {
        invitationService = plugin.provide(InvitationService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;

        String string = strings[1];
        if (invitationService.hasNoInvitation(player, string)) {
            player.sendMessage(Message.INVITATION_NULL.prefix(string));
            return;
        }

        Settlement settlement = settlementService.getSettlement(string);
        if (settlement == null) {
            player.sendMessage(Message.NULL.prefix(string));
            return;
        }

        invitationService.removeInvitation(player, strings[1]);
        player.sendMessage(Message.INVITATION_DENIED.prefix(string));
    }


    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) {
            Set<String> invitations = invitationService.getInvitations(player);
            if (invitations != null) return invitations.stream().toList();
        }
        return List.of();
    }
}