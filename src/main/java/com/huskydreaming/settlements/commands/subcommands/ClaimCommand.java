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
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CLAIM)
public class ClaimCommand implements CommandInterface {

    @Override
    public void run(Player player, String[] strings) {
        SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);

        Settlement settlement = settlementService.getSettlement(player);
        if (settlement == null) {
            player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        if(settlementService.isSettlement(player.getLocation().getChunk())) {
            player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_LAND_CLAIMED));
            return;
        }

        Role role = settlement.getRole(player);
        player.sendMessage(role.getName());

        if(role.hasPermission(RolePermission.LAND_CLAIM) || settlement.isOwner(player)) {
            Chunk chunk = player.getLocation().getChunk();

            String x = String.valueOf(chunk.getX());
            String z = String.valueOf(chunk.getZ());

            player.sendMessage(Chat.parameterize(Locale.SETTLEMENT_LAND_CLAIM, x, z));
            settlement.add(player.getLocation().getChunk());
        } else {
            player.sendMessage(Chat.parameterize(Locale.NO_PERMISSIONS, RolePermission.LAND_CLAIM.getName()));
        }
    }
}
