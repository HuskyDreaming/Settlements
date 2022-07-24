package com.huskydreaming.settlements.commands.subcommands;

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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.KICK)
public class KickCommand implements CommandInterface {

    private final CitizenService citizenService;
    private final SettlementService settlementService;

    public KickCommand(CitizenService citizenService, SettlementService settlementService) {
        this.citizenService = citizenService;
        this.settlementService = settlementService;
    }

    @Override
    public void run(Player player, String[] strings) {
        if(strings.length == 2) {
            OfflinePlayer offlinePlayer = Remote.getOfflinePlayer(strings[1]);
            if (offlinePlayer == null) {
                player.sendMessage(Remote.prefix(Locale.PLAYER_NULL, strings[1]));
                return;
            }

            if (!citizenService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_NULL));
                return;
            }

            Citizen citizen = citizenService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(citizen.getSettlement());
            Role role = settlement.getRole(citizen.getRole());

            if (!(role.hasPermission(RolePermission.CITIZEN_KICK) || settlement.isOwner(player))) {
                player.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS, RolePermission.CITIZEN_KICK.getName()));
                return;
            }

            if(role.hasPermission(RolePermission.CITIZEN_KICK_EXEMPT) || settlement.isOwner(offlinePlayer)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK_EXEMPT));
            } else {
                citizenService.remove(offlinePlayer);
                Player onlinePlayer = offlinePlayer.getPlayer();
                if(onlinePlayer != null) {
                    onlinePlayer.sendMessage(Remote.prefix(Locale.SETTLEMENT_KICK));
                }
            }
        }
    }
}
