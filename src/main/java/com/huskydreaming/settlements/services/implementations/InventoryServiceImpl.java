package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.providers.*;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.base.ServiceProvider;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.ClaimService;
import com.huskydreaming.settlements.services.interfaces.InventoryService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {

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
        RoleService roleService = ServiceProvider.Provide(RoleService.class);
        int rows = (int) Math.ceil((double) roleService.getRoles(settlement).size() / 9);
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
        ClaimService claimService = ServiceProvider.Provide(ClaimService.class);
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
        MemberService memberService = ServiceProvider.Provide(MemberService.class);
        List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlement);
        OfflinePlayer[] array = offlinePlayers.toArray(new OfflinePlayer[0]);
        int rows = (int) Math.ceil((double) offlinePlayers.size() / 9);
        MembersInventory membersInventory = new MembersInventory(settlement, rows, array);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("membersInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(membersInventory)
                .title("Members")
                .build();
    }

    @Override
    public SmartInventory getCitizenInventory(Settlement settlement, OfflinePlayer offlinePlayer) {
        MemberInventory memberInventory = new MemberInventory(settlement, offlinePlayer);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("memberInventory")
                .size(3, 9)
                .provider(memberInventory)
                .title("Editing: " + offlinePlayer.getName())
                .build();
    }

    @Override
    public void deserialize(SettlementPlugin plugin) {
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }
}