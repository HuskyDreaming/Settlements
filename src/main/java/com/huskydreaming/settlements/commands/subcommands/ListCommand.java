package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Locale;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.LIST)
public class ListCommand implements SubCommand {

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
            if (settlementService.getSettlements().isEmpty()) {
                player.sendMessage(Locale.SETTLEMENT_LIST_NULL.prefix());
            } else {
                inventoryService.getSettlementsInventory(plugin).open(player);
            }
        }
    }
}