package com.huskydreaming.settlements.services.providers;

import com.huskydreaming.settlements.SettlementPlugin;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.inventories.providers.*;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager inventoryManager;

    @Override
    public SmartInventory getRoleInventory(SettlementPlugin plugin, String name, Role role) {
        int rows = (int) Math.ceil((double) RolePermission.values().length / 9);
        RoleInventory roleInventory = new RoleInventory(plugin, name, rows, role);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("roleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(roleInventory)
                .title("Editing: " + role.getName())
                .build();
    }

    @Override
    public SmartInventory getSettlementInventory(SettlementPlugin plugin, String name) {
        SettlementInventory settlementInventory = new SettlementInventory(plugin, name);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("settlementInventory")
                .size(3, 9)
                .provider(settlementInventory)
                .title("Editing: " + name)
                .build();
    }

    @Override
    public SmartInventory getSettlementsInventory(SettlementPlugin plugin) {
        SettlementService settlementService = plugin.provide(SettlementService.class);
        String[] settlements = settlementService.getSettlements().keySet().toArray(new String[0]);
        int rows = (int) Math.ceil((double) settlements.length / 9);
        SettlementsInventory settlementsInventory = new SettlementsInventory(plugin, rows, settlements);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("settlementsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(settlementsInventory)
                .title("Settlements")
                .build();
    }

    @Override
    public SmartInventory getConfirmationInventory(SettlementPlugin plugin, String settlementName, InventoryAction inventoryAction) {
        ConfirmationInventory confirmationInventory = new ConfirmationInventory(plugin, settlementName, inventoryAction);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("confirmationInventory")
                .size(3, 9)
                .provider(confirmationInventory)
                .title(inventoryAction.getTitle())
                .build();
    }

    @Override
    public SmartInventory getRolesInventory(SettlementPlugin plugin, String settlementName) {
        RoleService roleService = plugin.provide(RoleService.class);
        int rows = (int) Math.ceil((double) roleService.getRoles(settlementName).size() / 9);
        RolesInventory rolesInventory = new RolesInventory(plugin, settlementName, rows);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("rolesInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(rolesInventory)
                .title("Roles")
                .build();
    }

    @Override
    public SmartInventory getClaimsInventory(SettlementPlugin plugin, String settlementName) {
        ClaimService claimService = plugin.provide(ClaimService.class);
        Collection<String> chunks = claimService.getChunksAsStrings(settlementName);
        String[] array = chunks.toArray(new String[0]);
        int rows = (int) Math.ceil((double) array.length / 9);
        ClaimsInventory claimsInventory = new ClaimsInventory(plugin, settlementName, rows, array);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("landsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(claimsInventory)
                .title("Lands")
                .build();
    }

    @Override
    public SmartInventory getCitizensInventory(SettlementPlugin plugin, String settlementName) {
        MemberService memberService = plugin.provide(MemberService.class);
        List<OfflinePlayer> offlinePlayers = memberService.getOfflinePlayers(settlementName);
        OfflinePlayer[] array = offlinePlayers.toArray(new OfflinePlayer[0]);
        int rows = (int) Math.ceil((double) offlinePlayers.size() / 9);
        MembersInventory membersInventory = new MembersInventory(plugin, settlementName, rows, array);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("membersInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(membersInventory)
                .title("Members")
                .build();
    }

    @Override
    public SmartInventory getCitizenInventory(SettlementPlugin plugin, String settlementName, OfflinePlayer offlinePlayer) {
        MemberInventory memberInventory = new MemberInventory(plugin, settlementName, offlinePlayer);
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