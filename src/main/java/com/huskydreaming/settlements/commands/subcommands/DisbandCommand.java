package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.DISBAND)
public class DisbandCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (!settlementService.hasSettlement(player)) {
            player.sendMessage("You are not part of a settlement.");
            return;
        }

        Settlement settlement = settlementService.getSettlement(player);
        if(!settlement.isOwner(player)) {
            player.sendMessage("You must be owner to disband your settlement.");
            return;
        }

        player.sendMessage("You have disbanded your settlement.");
        settlementService.disbandSettlement(settlement);
    }
}
