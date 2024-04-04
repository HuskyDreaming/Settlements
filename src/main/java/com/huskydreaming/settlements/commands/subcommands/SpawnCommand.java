package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SPAWN)
public class SpawnCommand implements SubCommand {

    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public SpawnCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Locale.SETTLEMENT_PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        Role role = roleService.getRole(member);

        if (!(role.hasPermission(RolePermission.SPAWN_TELEPORT) || settlement.isOwner(player))) {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix(RolePermission.SPAWN_TELEPORT));
            return;
        }

        if (settlement.getLocation() == null) {
            player.sendMessage(Locale.SETTLEMENT_SPAWN_NULL.prefix());
            return;
        }

        player.teleport(settlement.getLocation());
        player.sendMessage(Locale.SETTLEMENT_SPAWN.prefix());
    }
}