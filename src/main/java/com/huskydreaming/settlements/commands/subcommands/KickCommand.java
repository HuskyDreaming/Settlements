package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.SettlementService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.KICK)
public class KickCommand implements CommandInterface {

    @Inject
    private SettlementService settlementService;

    @Override
    public void run(Player player, String[] strings) {

    }
}
