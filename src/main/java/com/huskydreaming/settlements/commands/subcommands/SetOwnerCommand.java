package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETOWNER, arguments = " [owner]")
public class SetOwnerCommand implements CommandInterface {

    private final MemberService memberService;
    private final SettlementService settlementService;

    public SetOwnerCommand(SettlementPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1];

            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            if (!settlement.isOwner(player)) {
                player.sendMessage(Locale.SETTLEMENT_NOT_OWNER.prefix());
                return;
            }

            OfflinePlayer offlinePlayer = Remote.getOfflinePlayer(string);
            if (offlinePlayer == null) {
                player.sendMessage(Locale.PLAYER_NULL.prefix(string));
                return;
            }

            if (offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Locale.SETTLEMENT_IS_OWNER.prefix());
                return;
            }

            Member offlineMember = memberService.getCitizen(offlinePlayer);
            if (!offlineMember.getSettlement().equalsIgnoreCase(member.getSettlement())) {
                player.sendMessage(Locale.SETTLEMENT_NOT_CITIZEN.prefix());
                return;
            }

            settlement.setOwner(offlinePlayer);
            player.sendMessage(Locale.SETTLEMENT_OWNER_TRANSFERRED.prefix(offlinePlayer.getName()));
        }
    }
}