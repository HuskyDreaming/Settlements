package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.database.entities.*;
import com.huskydreaming.settlements.enumeration.PermissionType;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.enumeration.locale.Message;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandAnnotation(label = CommandLabel.SET_HOME)
public class SetHomeCommand implements PlayerCommandProvider {

    private final ContainerService containerService;
    private final HomeService homeService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetHomeCommand(HuskyPlugin plugin) {
        containerService = plugin.provide(ContainerService.class);
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

        Set<Home> homes = homeService.getHomes(settlement);
        Container container = containerService.getContainer(settlement);
        int maxHomes = container.getMaxHomes();
        if (homes.size() >= maxHomes) {
            player.sendMessage(Message.HOME_MAX.prefix(maxHomes));
            return;
        }

        String string = strings[1];
        if(homeService.hasHome(settlement, string)) {
            player.sendMessage(Message.HOME_EXISTS.prefix(string));
            return;
        }

        homeService.setHome(settlement, player, string);
        player.sendMessage(Message.HOME_SET.prefix(string));
    }
}