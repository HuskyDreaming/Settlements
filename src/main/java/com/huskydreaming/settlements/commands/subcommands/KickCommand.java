package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.BorderService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAnnotation(label = CommandLabel.KICK, arguments = " [player]")
public class KickCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public KickCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;

        String string = strings[1];
        OfflinePlayer offlinePlayer = Util.getOfflinePlayer(string);

        if (offlinePlayer == null) {
            player.sendMessage(Message.PLAYER_NULL.prefix(string));
            return;
        }

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);


        if(!(role.hasPermission(RolePermission.MEMBER_KICK) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Member offlineMember = memberService.getCitizen(offlinePlayer);
        Role offlineRole = roleService.getRole(offlineMember);

        if (offlineRole.hasPermission(RolePermission.MEMBER_KICK_EXEMPT) || settlement.isOwner(offlinePlayer)) {
            player.sendMessage(Message.KICK_EXEMPT.prefix());
            return;
        }

        memberService.remove(offlinePlayer);

        Player onlinePlayer = offlinePlayer.getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.closeInventory();
            borderService.removePlayer(onlinePlayer);
            borderService.addPlayer(onlinePlayer, member.getSettlement(), Color.RED);
            onlinePlayer.sendMessage(Message.KICK.parameterize(member.getSettlement()));
        }

        for (OfflinePlayer offline : memberService.getOfflinePlayers(member.getSettlement())) {
            if (!offline.isOnline()) return;
            Player on = offline.getPlayer();

            if (on == null) return;
            on.sendMessage(Message.LEAVE_PLAYER.prefix(offlinePlayer.getName()));
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if(strings.length == 2 && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            return memberService.getOfflinePlayers(member.getSettlement()).stream().map(OfflinePlayer::getName).toList();
        }
        return List.of();
    }
}