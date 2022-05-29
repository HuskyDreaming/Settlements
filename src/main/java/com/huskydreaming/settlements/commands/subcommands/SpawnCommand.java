package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.entity.Player;

public class SpawnCommand extends CommandBase {

    public SpawnCommand() {
        super("spawn");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        SettlementManager settlementManager = settlements.getSettlementManager();
        if (settlementManager.hasSettlement(player)) {
            player.sendMessage("You do not seem to belong to a settlement.");
            return;
        }

        Settlement settlement = settlementManager.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.SPAWN_TELEPORT) || settlement.isOwner(player))) {
            player.sendMessage("You do not have permission for this.");
            return;
        }

        player.teleport(settlement.getLocation());
        player.sendMessage("You have teleported to settlement spawn.");
    }
}
