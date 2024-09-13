package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.Home;
import com.huskydreaming.settlements.database.entities.Member;
import com.huskydreaming.settlements.database.entities.Role;
import com.huskydreaming.settlements.database.entities.Settlement;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;

import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.DELETE_HOME)
public class DeleteHomeCommand implements PlayerCommandProvider {

    private final HomeService homeService;
    private final PermissionService permissionService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DeleteHomeCommand(HuskyPlugin plugin) {
        homeService = plugin.provide(HomeService.class);
        memberService = plugin.provide(MemberService.class);
        permissionService = plugin.provide(PermissionService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getMember(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if (!(permissions.contains(PermissionType.EDIT_HOMES) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        String home = strings[1];
        if (!homeService.hasHome(settlement, home)) {
            player.sendMessage(Message.HOME_NULL.prefix(Util.capitalize(home)));
            return;
        }

        homeService.deleteHome(settlement, home);
        player.sendMessage(Message.HOME_DELETE.prefix(home));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length != 2) return List.of();
        if (!memberService.hasSettlement(player)) return List.of();

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlementId());
        return homeService.getHomes(settlement).stream().map(Home::getName).collect(Collectors.toList());
    }
}