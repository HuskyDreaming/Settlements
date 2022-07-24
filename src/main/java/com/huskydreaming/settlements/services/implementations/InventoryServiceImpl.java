package com.huskydreaming.settlements.services.implementations;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.providers.*;
import com.huskydreaming.settlements.persistence.Citizen;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.CitizenService;
import com.huskydreaming.settlements.services.ClaimService;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.utilities.Remote;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

import java.util.Collection;

@Singleton
public class InventoryServiceImpl implements InventoryService {

    @Inject
    private SettlementPlugin plugin;

    @Inject
    private CitizenService citizenService;

    @Inject
    private ClaimService claimService;

    private InventoryManager inventoryManager;

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
        if(Remote.isInjected(plugin.getInjector(), settlementInventory)) {
            System.out.println("You suck dick");
        } else {
            plugin.getInjector().injectMembers(settlementInventory);
        }
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
    public SmartInventory getClaimsInventory(Settlement settlement) {
        Collection<String> chunks = claimService.getChunksAsStrings(settlement);
        String[] array = chunks.toArray(new String[0]);
        int rows = (int) Math.ceil((double) array.length / 9);
        ClaimsInventory claimsInventory = new ClaimsInventory(settlement, rows, array);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("landsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(claimsInventory)
                .title("Lands")
                .build();
    }

    @Override
    public SmartInventory getCitizensInventory(Settlement settlement) {
        Citizen[] citizens = citizenService.getCitizens(settlement).toArray(new Citizen[0]);
        int rows = (int) Math.ceil((double) citizens.length / 9);
        CitizensInventory citizensInventory = new CitizensInventory(settlement, rows, citizens);
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
        //CitizenInventory citizenInventory = new CitizenInventory(settlement, offlinePlayer);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("citizenInventory")
                .size(3, 9)
                //.provider(citizenInventory)
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
