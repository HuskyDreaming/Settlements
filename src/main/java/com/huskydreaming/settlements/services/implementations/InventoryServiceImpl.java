package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.providers.CitizenInventory;
import com.huskydreaming.settlements.inventories.providers.CitizensInventory;
import com.huskydreaming.settlements.inventories.providers.LandsInventory;
import com.huskydreaming.settlements.inventories.providers.LandsOwnerInventory;
import com.huskydreaming.settlements.inventories.providers.RoleInventory;
import com.huskydreaming.settlements.inventories.providers.RolesInventory;
import com.huskydreaming.settlements.inventories.providers.SettlementInventory;
import com.huskydreaming.settlements.inventories.providers.SettlementsInventory;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.InventoryService;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager inventoryManager;

    @Override
    public SmartInventory getSettlementsInventory(Settlement[] settlements) {
        int rows = (int) Math.ceil((double) settlements.length / 9);
        SettlementsInventory settlementsInventory = new SettlementsInventory(rows, settlements);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("settlementsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(settlementsInventory)
                .title("Settlements")
                .build();
    }

    @Override
    public SmartInventory getRoleInventory(Settlement settlement, Role role) {
        int rows = (int) Math.ceil((double) RolePermission.values().length / 9);
        RoleInventory roleInventory = new RoleInventory(settlement, rows, role);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("roleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(roleInventory)
                .title("Editing: " + role.getName())
                .build();
    }

    @Override
    public SmartInventory getSettlementInventory(Settlement settlement) {
        SettlementInventory settlementInventory = new SettlementInventory(settlement);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("settlementInventory")
                .size(3, 9)
                .provider(settlementInventory)
                .title("Editing: " + settlement.getName())
                .build();
    }

    @Override
    public SmartInventory getRolesInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getRoles().size() / 9);
        RolesInventory rolesInventory = new RolesInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("rolesInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(rolesInventory)
                .title("Roles")
                .build();
    }

    @Override
    public SmartInventory getLandsInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getLands().size() / 9);
        LandsInventory landsInventory = new LandsInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("landsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(landsInventory)
                .title("Lands")
                .build();
    }

    @Override
    public SmartInventory getLandsOwnerInventory(Settlement settlement, Land land) {
        int rows = (int) Math.ceil((double) settlement.getCitizens().length / 9);
        LandsOwnerInventory landsOwnerInventory = new LandsOwnerInventory(settlement, rows, land);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("landsOwnerInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(landsOwnerInventory)
                .title("Set Land Owner")
                .build();
    }

    @Override
    public SmartInventory getCitizensInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getCitizens().length / 9);
        CitizensInventory citizensInventory = new CitizensInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("citizensInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(citizensInventory)
                .title("Citizens")
                .build();
    }

    @Override
    public SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer) {
        CitizenInventory citizenInventory = new CitizenInventory(settlement, offlinePlayer);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("citizenInventory")
                .size(3, 9)
                .provider(citizenInventory)
                .title("Editing: " + offlinePlayer.getName())
                .build();
    }

    @Override
    public void serialize(SettlementPlugin plugin) {

    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }
}
