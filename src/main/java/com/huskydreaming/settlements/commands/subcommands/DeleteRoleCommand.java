package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Member;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DELETEROLE)
public class DeleteRoleCommand implements CommandInterface {

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public DeleteRoleCommand() {
        memberService = ServiceProvider.Provide(MemberService.class);
        roleService = ServiceProvider.Provide(RoleService.class);
        settlementService = ServiceProvider.Provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if(!memberService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            Role role = roleService.getRole(settlement, member);

            if(role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player)) {
                String roleName = strings[1];
                if(!roleService.hasRole(settlement, roleName)) {
                    player.sendMessage(Remote.prefix(Locale.SETTLEMENT_ROLE_NULL, roleName));
                    return;
                }

                Role roleToDelete = roleService.getRole(settlement, roleName);

                roleService.remove(settlement, roleToDelete);
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_ROLE_DELETE, roleName));
            } else {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.EDIT_ROLES.getName()));
            }
        }
    }
}