package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@CommandAnnotation(label = CommandLabel.KICK, arguments = " [player]")
public class KickCommand implements PlayerCommandProvider {

    private final BorderService borderService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public KickCommand(SettlementPlugin plugin) {
        borderService = plugin.provide(BorderService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
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

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.MEMBER_KICK) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Member offlineMember = memberService.getMember(offlinePlayer);
        Role offlineRole = roleService.getRole(offlineMember);

        Set<PermissionType> offlinePermissions = permissionService.getPermissions(offlineRole);
        if (offlinePermissions.contains(PermissionType.MEMBER_KICK_EXEMPT) || settlement.isOwner(offlinePlayer)) {
            player.sendMessage(Message.KICK_EXEMPT.prefix());
            return;
        }

        memberService.remove(offlinePlayer);

        Player onlinePlayer = offlinePlayer.getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.closeInventory();
            borderService.removePlayer(onlinePlayer);
            borderService.addPlayer(onlinePlayer, settlement, Color.RED);
            onlinePlayer.sendMessage(Message.KICK.parameterize(settlement.getName()));
        }

        for (OfflinePlayer offline : memberService.getOfflinePlayers(settlement)) {
            if (!offline.isOnline()) return;
            Player on = offline.getPlayer();

            if (on == null) return;
            on.sendMessage(Message.LEAVE_PLAYER.prefix(offlinePlayer.getName()));
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if(strings.length == 2 && memberService.hasSettlement(player)) {
            Member member = memberService.getMember(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlementId());
            return memberService.getOfflinePlayers(settlement).stream().map(OfflinePlayer::getName).toList();
        }
        return List.of();
    }
}