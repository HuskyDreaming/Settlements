package com.huskydreaming.settlements.commands.subcommands;

import com.google.inject.Inject;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.InventoryService;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.LIST)
public class ListCommand implements CommandInterface {

    @Inject
    private InventoryService inventoryService;

    @Override
    public void run(Player player, String[] strings) {
        inventoryService.getSettlementsInventory().open(player);
    }
}
