package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.PermissionService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.SET_ROLE, arguments = " [player] [role]")
public class SetRoleCommand implements PlayerCommandProvider {

    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetRoleCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 3) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if (!(permissions.contains(PermissionType.EDIT_ROLES) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        OfflinePlayer offlinePlayer = Util.getOfflinePlayer(strings[1]);
        if (offlinePlayer == null) {
            player.sendMessage(Message.PLAYER_OFFLINE.prefix(strings[1]));
            return;
        }

        Role setRole = roleService.getRole(settlement, strings[2]);
        if (setRole == null) {
            player.sendMessage(Message.ROLE_NULL.prefix(strings[1]));
            return;
        }

        member.setRoleId(setRole.getId());
        player.sendMessage(Message.ROLE_SET.prefix(offlinePlayer.getName(), setRole.getName()));

        if (!offlinePlayer.isOnline()) return;
        Player onlinePlayer = offlinePlayer.getPlayer();

        if (onlinePlayer == null) return;
        onlinePlayer.sendMessage(Message.ROLE_SET_OTHER.prefix(offlinePlayer.getName(), setRole.getName()));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) return List.of();
        if (strings.length < 2 || strings.length > 3) return List.of();

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member);
        if (strings.length == 2) {
            return memberService.getOfflinePlayers(settlement)
                    .stream()
                    .map(OfflinePlayer::getName)
                    .collect(Collectors.toList());
        }

        return roleService.getRoles(settlement)
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}