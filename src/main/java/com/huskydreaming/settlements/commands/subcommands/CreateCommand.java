package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.utilities.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CREATE)
public class CreateCommand implements CommandInterface {

    @Inject
    private CitizenService citizenService;

    @Inject
    private ClaimService claimService;

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            if(citizenService.hasSettlement(player)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_PLAYER_EXISTS));
                return;
            }

            if (settlementService.isSettlement(strings[1])) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_EXIST));
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            if (claimService.isClaim(chunk)) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_ESTABLISHED));
                return;
            }

            Settlement settlement = settlementService.createSettlement(player, strings[1]);
            citizenService.add(player, settlement);
            claimService.setClaim(chunk, settlement);

            player.sendMessage(Remote.prefix(Locale.SETTLEMENT_CREATED, strings[1]));
        }
    }
}
