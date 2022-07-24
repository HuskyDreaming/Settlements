package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SPAWN)
public class SpawnCommand implements CommandInterface {

    @Inject
    private CitizenService citizenService;

    @Inject
    private SettlementService settlementService;

    public SpawnCommand(CitizenService citizenService, SettlementService settlementService) {
        this.citizenService = citizenService;
        this.settlementService = settlementService;
    }

    @Override
    public void run(Player player, String[] strings) {
        if (!citizenService.hasSettlement(player)) {
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
            return;
        }

        Citizen citizen = citizenService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
        Role role = settlement.getRole(citizen.getRole());

        if (!(role.hasPermission(RolePermission.SPAWN_TELEPORT) || settlement.isOwner(player))) {
            player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS), RolePermission.SPAWN_TELEPORT.getName());
            return;
        }

        player.teleport(settlement.getLocation());
        player.sendMessage(Remote.prefix(Locale.SETTLEMENT_SPAWN));
    }
}
