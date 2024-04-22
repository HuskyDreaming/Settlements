package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.DELETE_ROLE, arguments = " [role]")
public class DeleteRoleCommand implements PlayerCommandProvider {

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DeleteRoleCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
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

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if(!(role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        String roleName = strings[1];
        if (!roleService.hasRole(member.getSettlement(), roleName)) {
            player.sendMessage(Message.ROLE_NULL.prefix(roleName));
            return;
        }

        Role roleToDelete = roleService.getRole(member.getSettlement(), roleName);

        roleService.remove(member.getSettlement(), roleToDelete);
        player.sendMessage(Message.ROLE_DELETE.prefix(roleName));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length != 2 || !memberService.hasSettlement(player)) return new ArrayList<>();

        Member member = memberService.getCitizen(player);
        return roleService.getRoles(member.getSettlement()).stream().map(Role::getName).collect(Collectors.toList());
    }
}