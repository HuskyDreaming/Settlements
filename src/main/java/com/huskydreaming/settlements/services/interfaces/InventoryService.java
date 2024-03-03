package com.huskydreaming.settlements.services.interfaces;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public interface InventoryService extends ServiceInterface {

    SmartInventory getRoleInventory(SettlementPlugin plugin, String name, Role role);

    SmartInventory getSettlementInventory(SettlementPlugin plugin, String name);

    SmartInventory getSettlementsInventory(SettlementPlugin plugin);

    SmartInventory getConfirmationInventory(SettlementPlugin plugin, String settlementName, InventoryAction inventoryAction);

    SmartInventory getRolesInventory(SettlementPlugin plugin, String settlementName);

    SmartInventory getClaimsInventory(SettlementPlugin plugin, String settlementName);

    SmartInventory getCitizensInventory(SettlementPlugin plugin, String settlementName);

    SmartInventory getCitizenInventory(SettlementPlugin plugin, String settlementName, OfflinePlayer offlinePlayer);
}