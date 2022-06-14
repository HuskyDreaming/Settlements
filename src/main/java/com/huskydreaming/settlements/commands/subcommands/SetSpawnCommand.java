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

@Command(label = CommandLabel.SETSPAWN)
public class SetSpawnCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (!settlementService.hasSettlement(player)) {
            player.sendMessage("You do not seem to belong to a settlement.");
            return;
        }

        Settlement settlement = settlementService.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.SPAWN_SET) || settlement.isOwner(player))) {
            player.sendMessage("You do not seem to have permissions.");
            return;
        }

        settlement.setLocation(player.getLocation());
        player.sendMessage("You have set the settlement spawn at your location.");
    }
}
