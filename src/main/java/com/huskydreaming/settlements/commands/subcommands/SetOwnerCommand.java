package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.SettlementManager;
import com.huskydreaming.settlements.persistence.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetOwnerCommand extends CommandBase {

    public SetOwnerCommand() {
        super("setowner");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        if (strings.length == 2) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                player.sendMessage("The player " + strings[0] + " does not seem to be online.");
                return;
            }

            SettlementManager settlementManager = settlements.getSettlementManager();
            if (!settlementManager.hasSettlement(player)) {
                player.sendMessage("You do not seem to belong to a settlement.");
                return;
            }

            Settlement settlement = settlementManager.getSettlement(player);
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
