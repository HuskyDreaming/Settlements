package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.enumerations.Locale;
import com.huskydreaming.settlements.utilities.Remote;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.LIST)
public class ListCommand implements CommandInterface {

    private final SettlementPlugin plugin;
    private final InventoryService inventoryService;
    private final SettlementService settlementService;

    public ListCommand(SettlementPlugin plugin) {
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 1) {
            if(settlementService.getSettlements().isEmpty()) {
                player.sendMessage(Remote.prefix(Locale.SETTLEMENT_LIST_NULL));
            } else {
                inventoryService.getSettlementsInventory(plugin).open(player);
            }
        }
    }
}