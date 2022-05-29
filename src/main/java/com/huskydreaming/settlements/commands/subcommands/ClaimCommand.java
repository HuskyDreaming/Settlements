package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ClaimCommand extends CommandBase {

    public ClaimCommand() {
        super("claim");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        SettlementManager settlementManager = settlements.getSettlementManager();
        Settlement settlement = settlementManager.getSettlement(player);
        if (settlement == null) {
            player.sendMessage("You do not seem to belong to a settlement.");
            return;
        }

        if(settlementManager.isClaimed(player.getLocation().getChunk())) {
            player.sendMessage("This land has already been claimed by another settlement.");
            return;
        }

        Role role = settlement.getRole(player);
        player.sendMessage(role.getName());

        if(role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            Chunk chunk = player.getLocation().getChunk();
            player.sendMessage("You have claimed new land ( " + chunk.getX() + ", " + chunk.getZ() + " ).");
            settlement.add(player.getLocation().getChunk());
        } else {
            player.sendMessage("You do not have permissions to claim land.");
        }
    }
}
