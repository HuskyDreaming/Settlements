package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.VisualisationService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.SHOW)
public class ShowCommand implements CommandInterface {

    @Inject
    private VisualisationService visualisationService;

    @Override
    public void run(Player player, String[] strings) {
        if (visualisationService.isVisualiser(player)) {
            visualisationService.removeVisualiser(player);
            player.sendMessage("Visualisation turned off.");
        } else {
            visualisationService.addVisualiser(player);
            player.sendMessage("Visualisation turned on.");
        }
    }
}
