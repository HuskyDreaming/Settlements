package com.huskydreaming.settlements.services;

import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.services.base.Service;
import com.huskydreaming.settlements.services.base.ServiceInterface;
import com.huskydreaming.settlements.services.base.ServiceType;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

@Service(type = ServiceType.INVENTORY)
public interface InventoryService extends ServiceInterface {

    SmartInventory getSettlementsInventory(Settlement[] settlements);

    SmartInventory getRoleInventory(Settlement settlement, Role role);

    SmartInventory getSettlementInventory(Settlement settlement);

    SmartInventory getRolesInventory(Settlement settlement);

    SmartInventory getLandsInventory(Settlement settlement);

    SmartInventory getLandsOwnerInventory(Settlement settlement, Land land);

    SmartInventory getCitizensInventory(Settlement settlement);

    SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer);
}
