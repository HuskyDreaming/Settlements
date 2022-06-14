package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.persistence.roles.Role;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.OfflinePlayer;

public interface InventoryService {

    SmartInventory openInventory(InventoryProvider inventoryProvider);

    SmartInventory getSettlementsInventory();

    SmartInventory getRoleInventory(Settlement settlement, Role role);

    SmartInventory getSettlementInventory(Settlement settlement);

    SmartInventory getRolesInventory(Settlement settlement);

    SmartInventory getLandsInventory(Settlement settlement);

    SmartInventory getLandsOwnerInventory(Settlement settlement, Land land);

    SmartInventory getCitizensInventory(Settlement settlement);

    SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer);
}
