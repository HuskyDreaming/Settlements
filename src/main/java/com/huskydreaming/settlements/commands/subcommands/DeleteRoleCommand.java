package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DELETEROLE, arguments = " [role]")
public class DeleteRoleCommand implements CommandInterface {

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DeleteRoleCommand(SettlementPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if (!memberService.hasSettlement(player)) {
                player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(member);

            if (role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player)) {
                String roleName = strings[1];
                if (!roleService.hasRole(member.getSettlement(), roleName)) {
                    player.sendMessage(Locale.SETTLEMENT_ROLE_NULL.prefix(roleName));
                    return;
                }

                Role roleToDelete = roleService.getRole(member.getSettlement(), roleName);

                roleService.remove(member.getSettlement(), roleToDelete);
                player.sendMessage(Locale.SETTLEMENT_ROLE_DELETE.prefix(roleName));
            } else {
                player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.EDIT_ROLES));
            }
        }
    }
}