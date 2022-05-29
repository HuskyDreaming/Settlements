package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends CommandBase {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        SettlementManager settlementManager = settlements.getSettlementManager();
        if (!settlementManager.hasSettlement(player)) {
            player.sendMessage("You do not seem to belong to a settlement.");
            return;
        }

        Settlement settlement = settlementManager.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.SPAWN_SET) || settlement.isOwner(player))) {
            player.sendMessage("You do not seem to have permissions.");
            return;
        }

        settlement.setLocation(player.getLocation());
        player.sendMessage("You have set the settlement spawn at your location.");
    }
}
