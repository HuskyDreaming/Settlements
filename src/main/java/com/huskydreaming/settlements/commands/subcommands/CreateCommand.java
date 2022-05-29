package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import org.bukkit.entity.Player;

public class CreateCommand extends CommandBase {

    public CreateCommand() {
        super("create", "c");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        if (strings.length == 2) {
            SettlementManager settlementManager = settlements.getSettlementManager();
            String string = strings[1];

            if (settlementManager.exists(string)) {
                player.sendMessage("A settlement with that name already exists.");
                return;
            }

            if (settlementManager.hasSettlement(player)) {
                player.sendMessage("You are already part of a settlement.");
                return;
            }

            if (settlementManager.isClaimed(player.getLocation().getChunk())) {
                player.sendMessage("A settlement is already established here...");
                return;
            }

            player.sendMessage("You have created a new settlement called " + string + ".");
            settlementManager.create(player, string);
        }
    }
}
