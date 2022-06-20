package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.Chat;
import com.huskydreaming.settlements.utilities.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETSPAWN)
public class SetSpawnCommand implements CommandInterface {

    @Override
    public void run(Player player, String[] strings) {
        SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
        if (!settlementService.hasSettlement(player)) {
            player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Settlement settlement = settlementService.getSettlement(player);
        Role role = settlement.getRole(player);

        if (!(role.hasPermission(RolePermission.SPAWN_SET) || settlement.isOwner(player))) {
            player.sendMessage(Chat.parameterize(Locale.NO_PERMISSIONS), RolePermission.SPAWN_SET.getName());
            return;
        }

        settlement.setLocation(player.getLocation());
        player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_SET_SPAWN));
    }
}
