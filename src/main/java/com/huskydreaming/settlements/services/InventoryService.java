package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public interface InventoryService extends ServiceInterface {

    SmartInventory getRoleInventory(Settlement settlement, Role role);

    SmartInventory getSettlementInventory(Settlement settlement);

    SmartInventory getRolesInventory(Settlement settlement);

    SmartInventory getClaimsInventory(Settlement settlement);

    SmartInventory getCitizensInventory(Settlement settlement);

    SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer);
}
