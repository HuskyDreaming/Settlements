package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.DELETE_ROLE, arguments = " [role]")
public class DeleteRoleCommand implements PlayerCommandProvider {

    private final MemberService memberService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DeleteRoleCommand(HuskyPlugin plugin) {
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

        String roleName = strings[1];
        if (!roleService.hasRole(settlement, roleName)) {
            player.sendMessage(Message.ROLE_NULL.prefix(roleName));
            return;
        }

        roleService.removeRole(settlement, roleName);
        player.sendMessage(Message.ROLE_DELETE.prefix(roleName));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length != 2 || !memberService.hasSettlement(player)) return new ArrayList<>();

        Member member = memberService.getMember(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlementId());
        return roleService.getRoles(settlement).stream().map(Role::getName).collect(Collectors.toList());
    }
}