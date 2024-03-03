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
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETSPAWN)
public class SetSpawnCommand implements CommandInterface {

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SetSpawnCommand(SettlementPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if (!(role.hasPermission(RolePermission.SPAWN_SET) || settlement.isOwner(player))) {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS), RolePermission.SPAWN_SET.getName());
            return;
        }

        settlement.setLocation(player.getLocation());
        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_SET_SPAWN));
    }
}
