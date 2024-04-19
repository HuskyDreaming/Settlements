package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SetRoleCommand implements PlayerCommandProvider {

    private final MemberService memberService;
    private final RoleService roleService;

    private final SettlementService settlementService;

    public SetRoleCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 3) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());

        if (!(role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix());
            return;
        }

        OfflinePlayer offlinePlayer = Util.getOfflinePlayer(strings[1]);
        if (offlinePlayer == null) {
            player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
            return;
        }

        Role setRole = roleService.getRole(member.getSettlement(), strings[2]);
        if (setRole == null) {
            player.sendMessage(Locale.ROLE_NULL.prefix(strings[1]));
            return;
        }

        member.setRole(strings[2]);
        player.sendMessage(Locale.ROLE_SET.prefix(offlinePlayer.getName(), setRole.getName()));

        if (!offlinePlayer.isOnline()) return;
        Player onlinePlayer = offlinePlayer.getPlayer();

        if (onlinePlayer == null) return;
        onlinePlayer.sendMessage(Locale.ROLE_SET_OTHER.prefix(offlinePlayer.getName(), setRole.getName()));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) return List.of();
        if (strings.length < 2 || strings.length > 3) return List.of();

        Member member = memberService.getCitizen(player);
        if (strings.length == 2) {
            return memberService.getOfflinePlayers(member.getSettlement())
                    .stream()
                    .map(OfflinePlayer::getName)
                    .collect(Collectors.toList());
        }

        return roleService.getRoles(member.getSettlement())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}