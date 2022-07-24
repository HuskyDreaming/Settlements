package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETOWNER)
public class SetOwnerCommand implements CommandInterface {

    @Inject
    private CitizenService citizenService;

    @Inject
    private SettlementService settlementService;

    public SetOwnerCommand(CitizenService citizenService, SettlementService settlementService) {
        this.citizenService = citizenService;
        this.settlementService = settlementService;
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
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
            if (!settlement.isOwner(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_OWNER));
                return;
            }

            Citizen offlineCitizen = citizenService.getCitizen(offlinePlayer);
            if (!offlineCitizen.getSettlement().equalsIgnoreCase(citizen.getSettlement())) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_CITIZEN));
                return;
            }

            settlement.setOwner(offlinePlayer);
            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_NOT_CITIZEN), offlinePlayer.getName());
        }
    }
}
