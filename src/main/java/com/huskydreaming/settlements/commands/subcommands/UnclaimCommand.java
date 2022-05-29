package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.entity.Player;

public class UnclaimCommand extends CommandBase {

    public UnclaimCommand() {
        super("unclaim");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        SettlementManager settlementManager = settlements.getSettlementManager();
        if (!settlementManager.hasSettlement(player)) {
            player.sendMessage("You are not part of a settlement.");
            return;
        }

        Settlement settlement = settlementManager.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.LAND_UNCLAIM) || settlement.isOwner(player))) {
            player.sendMessage("You do not have permissions to unclaim land.");
            return;
        }

        settlement.remove(player.getLocation().getChunk());
        player.sendMessage("You have unclaimed the land you are standing on.");
    }
}
