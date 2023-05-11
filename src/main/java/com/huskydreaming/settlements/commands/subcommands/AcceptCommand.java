package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.InvitationService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.ACCEPT)
public class AcceptCommand implements CommandInterface {

    private final MemberService memberService;
    private final InvitationService invitationService;
    private final SettlementService settlementService;

    public AcceptCommand() {
        memberService = ServiceProvider.Provide(MemberService.class);
        invitationService = ServiceProvider.Provide(InvitationService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

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

            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_JOIN, string));
            invitationService.removeInvitation(player, string);
            memberService.add(player, settlement);
        }
    }
}
