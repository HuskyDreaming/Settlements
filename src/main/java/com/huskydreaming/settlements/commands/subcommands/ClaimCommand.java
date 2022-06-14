package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;


    @Override
    public void run(Player player, String[] strings) {
        Settlement settlement = settlementService.getSettlement(player);
        if (settlement == null) {
            player.sendMessage("You do not seem to belong to a settlement.");
            return;
        }

        if(settlementService.isSettlement(player.getLocation().getChunk())) {
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
