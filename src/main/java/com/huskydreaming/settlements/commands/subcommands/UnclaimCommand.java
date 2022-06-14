package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.UNCLAIM)
public class UnclaimCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (!settlementService.hasSettlement(player)) {
            player.sendMessage("You are not part of a settlement.");
            return;
        }

        Settlement settlement = settlementService.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.LAND_UNCLAIM) || settlement.isOwner(player))) {
            player.sendMessage("You do not have permissions to unclaim land.");
            return;
        }

        settlement.remove(player.getLocation().getChunk());
        player.sendMessage("You have unclaimed the land you are standing on.");
    }
}
