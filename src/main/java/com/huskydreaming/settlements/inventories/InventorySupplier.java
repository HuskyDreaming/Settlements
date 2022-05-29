package com.huskydreaming.settlements.inventories;

import com.huskydreaming.settlements.Settlements;
import com.huskydreaming.settlements.inventories.citizens.CitizenInventory;
import com.huskydreaming.settlements.inventories.citizens.CitizensInventory;
import com.huskydreaming.settlements.inventories.lands.LandsInventory;
import com.huskydreaming.settlements.inventories.lands.LandsOwnerInventory;
import com.huskydreaming.settlements.inventories.role.RoleInventory;
import com.huskydreaming.settlements.inventories.role.RolesInventory;
import com.huskydreaming.settlements.inventories.settlement.SettlementInventory;
import com.huskydreaming.settlements.inventories.settlement.SettlementsInventory;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.lands.Land;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

public class InventorySupplier {

    public static SmartInventory getSettlementsInventory() {
        Settlements settlements = Settlements.getInstance();
        int rows = (int) Math.ceil((double) settlements.getSettlementManager().getSettlements().size() / 9);
        SettlementsInventory settlementsInventory = new SettlementsInventory(rows, settlements);
        return SmartInventory.builder()
                .manager(settlements.getInventoryManager())
                .id("settlementsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(settlementsInventory)
                .title("Settlements")
                .build();
    }

    public static SmartInventory getRoleInventory(Settlement settlement, Role role) {
        int rows = (int) Math.ceil((double) RolePermission.values().length / 9);
        RoleInventory roleInventory = new RoleInventory(settlement, rows, role);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("roleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(roleInventory)
                .title("Editing: " + role.getName())
                .build();
    }

    public static SmartInventory getSettlementInventory(Settlement settlement) {
        SettlementInventory settlementInventory = new SettlementInventory(settlement);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("settlementInventory")
                .size(3, 9)
                .provider(settlementInventory)
                .title("Editing: " + settlement.getName())
                .build();
    }

    public static SmartInventory getRolesInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getRoles().size() / 9);
        RolesInventory rolesInventory = new RolesInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("rolesInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(rolesInventory)
                .title("Roles")
                .build();
    }

    public static SmartInventory getLandsInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getLands().size() / 9);
        LandsInventory landsInventory = new LandsInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("landsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(landsInventory)
                .title("Lands")
                .build();
    }

    public static SmartInventory getLandsOwnerInventory(Settlement settlement, Land land) {
        int rows = (int) Math.ceil((double) settlement.getCitizens().length / 9);
        LandsOwnerInventory landsOwnerInventory = new LandsOwnerInventory(settlement, rows, land);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("landsOwnerInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(landsOwnerInventory)
                .title("Set Land Owner")
                .build();
    }

    public static SmartInventory getCitizensInventory(Settlement settlement) {
        int rows = (int) Math.ceil((double) settlement.getCitizens().length / 9);
        CitizensInventory citizensInventory = new CitizensInventory(settlement, rows);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("citizensInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(citizensInventory)
                .title("Citizens")
                .build();
    }

    public static SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer) {
        CitizenInventory citizenInventory = new CitizenInventory(settlement, offlinePlayer);
        return SmartInventory.builder()
                .manager(Settlements.getInstance().getInventoryManager())
                .id("citizenInventory")
                .size(3, 9)
                .provider(citizenInventory)
                .title("Editing: " + offlinePlayer.getName())
                .build();
    }
}
