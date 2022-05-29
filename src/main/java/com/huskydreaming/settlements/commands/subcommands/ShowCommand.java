package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.commands.CommandBase;
import com.huskydreaming.settlements.managers.VisualisationManager;
import org.bukkit.entity.Player;

public class ShowCommand extends CommandBase {

    public ShowCommand() {
        super("show");
    }

    @Override
    public void run(Settlements settlements, Player player, String[] strings) {
        VisualisationManager visualisationManager = settlements.getVisualisationManager();
        if (visualisationManager.isVisualiser(player)) {
            visualisationManager.remove(player);
            player.sendMessage("Visualisation turned off.");
        } else {
            visualisationManager.add(player);
            player.sendMessage("Visualisation turned on.");
        }
    }
}
