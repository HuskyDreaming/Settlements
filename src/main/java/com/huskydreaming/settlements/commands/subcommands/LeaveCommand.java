package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAnnotation(label = CommandLabel.LEAVE)
public class LeaveCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final MemberService memberService;
    private final SettlementService settlementService;

    public LeaveCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if (settlement.isOwner(player)) {
            player.sendMessage(Locale.SETTLEMENT_LEAVE_OWNER.prefix());
            return;
        }

        memberService.remove(player);
        borderService.removePlayer(player);
        borderService.addPlayer(player, member.getSettlement(), Color.RED);
        player.sendMessage(Locale.SETTLEMENT_LEAVE.prefix());

        List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(member.getSettlement());
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (!offlinePlayer.isOnline()) return;
            Player onlinePlayer = offlinePlayer.getPlayer();

            if (onlinePlayer == null) return;
            onlinePlayer.sendMessage(Locale.SETTLEMENT_LEAVE_PLAYER.prefix(player.getName()));
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 1 && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            return List.of(member.getSettlement());
        }
        return List.of();
    }
}