package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.entity.Player;

public class DisbandCommand extends CommandBase {

    public DisbandCommand() {
        super("disband");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        SettlementManager settlementManager = settlements.getSettlementManager();
        if (!settlementManager.hasSettlement(player)) {
            player.sendMessage("You are not part of a settlement.");
            return;
        }

        Settlement settlement = settlementManager.getSettlement(player);
        if(!settlement.isOwner(player)) {
            player.sendMessage("You must be owner to disband your settlement.");
            return;
        }

        player.sendMessage("You have disbanded your settlement.");
        settlementManager.disband(settlement);
    }
}
