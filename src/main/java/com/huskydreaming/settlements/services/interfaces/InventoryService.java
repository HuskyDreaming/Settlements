package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public interface InventoryService extends ServiceInterface {

    SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer);
    SmartInventory getCitizensInventory(Settlement settlement);
    SmartInventory getClaimsInventory(Settlement settlement);
    SmartInventory getRoleInventory(Settlement settlement, Role role);

    SmartInventory getSettlementsInventory();

    SmartInventory getConfirmationInventory(Settlement settlement, InventoryAction inventoryAction);

    SmartInventory getRolesInventory(Settlement settlement);
    SmartInventory getSettlementInventory(Settlement settlement);

}
