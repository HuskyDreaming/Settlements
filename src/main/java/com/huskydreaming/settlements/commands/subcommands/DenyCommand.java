package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.enumeration.locale.Message;
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
        Settlement settlement = settlementService.getSettlement(strings[1]);
        if (settlement == null) {
            player.sendMessage(Message.NULL.prefix(strings[1]));
            return;
        }

        if (invitationService.hasNoInvitation(player, settlement)) {
            player.sendMessage(Message.INVITATION_NULL.prefix(settlement.getName()));
            return;
        }

        invitationService.removeInvitation(player, settlement);
        player.sendMessage(Message.INVITATION_DENIED.prefix(settlement.getName()));
    }


    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) {
            Set<Long> invitations = invitationService.getInvitations(player);
            return invitations.stream().map(Object::toString).toList();
        }
        return List.of();
    }
}