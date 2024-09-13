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

@CommandAnnotation(label = CommandLabel.CREATE_ROLE, arguments = " [role]")
public class CreateRoleCommand implements PlayerCommandProvider {

    private final ContainerService containerService;
    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public CreateRoleCommand(HuskyPlugin plugin) {
        containerService = plugin.provide(ContainerService.class);
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
        Settlement settlement = settlementService.getSettlement(member);
        Role role = roleService.getRole(member);

        Set<PermissionType> permissions = permissionService.getPermissions(role);
        if(!(permissions.contains(PermissionType.EDIT_ROLES) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        Set<Role> roles = roleService.getRoles(settlement);
        Container container = containerService.getContainer(settlement);
        int maxRoles = container.getMaxRoles();
        if (roles.size() >= maxRoles) {
            player.sendMessage(Message.ROLE_MAX.prefix(maxRoles));
            return;
        }

        String roleName = strings[1];
        if (roleService.hasRole(settlement, roleName)) {
            player.sendMessage(Message.ROLE_EXISTS.prefix(roleName));
            return;
        }

        roleService.addRole(settlement, roleName, 1);
        player.sendMessage(Message.ROLE_CREATE.prefix(roleName));
    }
}