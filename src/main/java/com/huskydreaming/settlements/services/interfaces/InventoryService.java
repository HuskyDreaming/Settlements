package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.persistence.roles.Role;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public interface InventoryService extends Service {

    SmartInventory getRoleInventory(HuskyPlugin plugin, String name, Role role);

    SmartInventory getSettlementInventory(HuskyPlugin plugin, String name);

    SmartInventory getSettlementsInventory(HuskyPlugin plugin);

    SmartInventory getConfirmationInventory(HuskyPlugin plugin, String settlementName, InventoryAction inventoryAction);

    SmartInventory getRolesInventory(HuskyPlugin plugin, String settlementName);

    SmartInventory getFlagsInventory(HuskyPlugin plugin, String settlementName);

    SmartInventory getClaimsInventory(HuskyPlugin plugin, String settlementName);

    SmartInventory getCitizensInventory(HuskyPlugin plugin, String settlementName);

    SmartInventory getCitizenInventory(HuskyPlugin plugin, String settlementName, OfflinePlayer offlinePlayer);
}