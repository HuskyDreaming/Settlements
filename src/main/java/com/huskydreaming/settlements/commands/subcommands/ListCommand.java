package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.settlements.commands.Command;
import com.huskydreaming.settlements.commands.CommandInterface;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import org.bukkit.entity.Player;

@Command(label = CommandLabel.LIST)
public class ListCommand implements CommandInterface {

    @Override
    public void run(Player player, String[] strings) {
        SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
        InventoryService inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);
        inventoryService.getSettlementsInventory(settlementService.getSettlements().toArray(new Settlement[0])).open(player);
    }
}
