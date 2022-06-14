package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SETOWNER)
public class SetOwnerCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                player.sendMessage("The player " + strings[0] + " does not seem to be online.");
                return;
            }

            if (!settlementService.hasSettlement(player)) {
                player.sendMessage("You do not seem to belong to a settlement.");
                return;
            }

            Settlement settlement = settlementService.getSettlement(player);
            if (!settlement.isOwner(player)) {
                player.sendMessage("You must be owner to perform this action.");
                return;
            }

            if (!settlement.isCitizen(target)) {
                player.sendMessage("The player is not a citizen of your settlement.");
                return;
            }

            settlement.setOwner(target);
            player.sendMessage("You have transferred ownership of your settlement to " + target.getName() + ".");
        }
    }
}
