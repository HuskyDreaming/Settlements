package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.CREATE)
public class CreateCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1];

            if (settlementService.isSettlement(string)) {
                player.sendMessage("A settlement with that name already exists.");
                return;
            }

            if (settlementService.hasSettlement(player)) {
                player.sendMessage("You are already part of a settlement.");
                return;
            }

            if (settlementService.isSettlement(player.getLocation().getChunk())) {
                player.sendMessage("A settlement is already established here...");
                return;
            }

            player.sendMessage("You have created a new settlement called " + string + ".");
            settlementService.createSettlement(player, string);
        }
    }
}
