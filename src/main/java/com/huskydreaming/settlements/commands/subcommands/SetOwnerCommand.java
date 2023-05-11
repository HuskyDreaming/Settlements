package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETOWNER)
public class SetOwnerCommand implements CommandInterface {

    private final MemberService memberService;
    private final SettlementService settlementService;

    public SetOwnerCommand() {
        memberService = ServiceProvider.Provide(MemberService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1];

            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            if (!settlement.isOwner(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_OWNER));
                return;
            }

            OfflinePlayer offlinePlayer = Remote.getOfflinePlayer(string);
            if (offlinePlayer == null) {
                player.sendMessage(Remote.prefix(Locale.PLAYER_NULL, string));
                return;
            }

            if(offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_IS_OWNER));
                return;
            }

            Member offlineMember = memberService.getCitizen(offlinePlayer);
            if (!offlineMember.getSettlement().equalsIgnoreCase(member.getSettlement())) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_CITIZEN));
                return;
            }

            settlement.setOwner(offlinePlayer);
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_OWNER_TRANSFERRED), offlinePlayer.getName());
        }
    }
}
