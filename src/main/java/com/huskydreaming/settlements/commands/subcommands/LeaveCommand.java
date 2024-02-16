package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.LEAVE)
public class LeaveCommand implements CommandInterface {

    private final BorderService borderService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public LeaveCommand() {
        borderService = ServiceProvider.Provide(BorderService.class);
        memberService = ServiceProvider.Provide(MemberService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if(!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if(settlement.isOwner(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LEAVE_OWNER));
            return;
        }
        String settlementName = settlement.getName();

        memberService.remove(player);
        borderService.removePlayer(player);
        borderService.addPlayer(player, settlementName, Color.RED);
        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LEAVE));

        List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlement);
        offlinePlayers.forEach(offlinePlayer ->  {
            if(offlinePlayer.isOnline()) {
                Player onlinePlayer = offlinePlayer.getPlayer();
                if(onlinePlayer != null) onlinePlayer.sendMessage(Remote.prefix(Locale.SETTLEMENT_LEAVE_PLAYER, player.getName()));
            }
        });
    }
}
