package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.services.interfaces.*;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

@CommandAnnotation(label = CommandLabel.LIST)
public class ListCommand implements PlayerCommandProvider {

    private final SettlementPlugin plugin;
    private final InventoryService inventoryService;
    private final SettlementService settlementService;

    public ListCommand(SettlementPlugin plugin) {
        this.plugin = plugin;

        inventoryService = plugin.provide(InventoryService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 1) return;

        if (settlementService.getSettlements().isEmpty()) {
            player.sendMessage(Message.GENERAL_NULL.prefix());
            return;
        }

        inventoryService.getSettlementsInventory(plugin).open(player);

    }
}