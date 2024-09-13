package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

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
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        if (settlement.isOwner(player)) {
            player.sendMessage(Message.LEAVE_OWNER.prefix());
            return;
        }

        memberService.remove(player);
        borderService.removePlayer(player);
        borderService.addPlayer(player, settlement, Color.RED);
        player.sendMessage(Message.LEAVE.prefix());

        Set<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlement);
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (!offlinePlayer.isOnline()) return;
            Player onlinePlayer = offlinePlayer.getPlayer();

            if (onlinePlayer == null) return;
            onlinePlayer.sendMessage(Message.LEAVE_PLAYER.prefix(player.getName()));
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 1 && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlementId());
            return List.of(settlement.getName());
        }
        return List.of();
    }
}