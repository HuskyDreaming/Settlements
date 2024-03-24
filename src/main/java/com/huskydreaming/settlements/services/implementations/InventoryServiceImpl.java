package com.huskydreaming.settlements.services.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.ChunkData;
import com.huskydreaming.settlements.inventories.InventoryAction;
import com.huskydreaming.settlements.inventories.providers.*;
import com.huskydreaming.settlements.persistence.Flag;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.interfaces.*;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager inventoryManager;

    @Override
    public SmartInventory getRoleInventory(HuskyPlugin plugin, String name, Role role) {
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
    public SmartInventory getSettlementInventory(HuskyPlugin plugin, String name) {
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
    public SmartInventory getSettlementsInventory(HuskyPlugin plugin) {
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
    public SmartInventory getConfirmationInventory(HuskyPlugin plugin, String settlementName, InventoryAction inventoryAction) {
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
    public SmartInventory getRolesInventory(HuskyPlugin plugin, String settlementName) {
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
    public SmartInventory getFlagsInventory(HuskyPlugin plugin, String settlementName) {
        Flag[] flags = Flag.values();
        int rows = (int) Math.ceil((double) flags.length / 9);
        FlagsInventory flagsInventory = new FlagsInventory(plugin, settlementName, rows, flags);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("flagsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(flagsInventory)
                .title("Flags")
                .build();
    }

    @Override
    public SmartInventory getClaimsInventory(HuskyPlugin plugin, String settlementName) {
        ChunkService chunkService = plugin.provide(ChunkService.class);
        ChunkData[] chunks = chunkService.getClaims(settlementName).toArray(new ChunkData[0]);
        int rows = (int) Math.ceil((double) chunks.length / 9);
        ClaimsInventory claimsInventory = new ClaimsInventory(plugin, settlementName, rows, chunks);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("claimsInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(claimsInventory)
                .title("Claims")
                .build();
    }

    @Override
    public SmartInventory getCitizensInventory(HuskyPlugin plugin, String settlementName) {
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
    public SmartInventory getCitizenInventory(HuskyPlugin plugin, String settlementName, OfflinePlayer offlinePlayer) {
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
    public void deserialize(HuskyPlugin plugin) {
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }
}