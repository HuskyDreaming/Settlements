package com.huskydreaming.settlements.inventories.providers;

import com.huskydreaming.settlements.inventories.InventoryItem;
import com.huskydreaming.settlements.persistence.Settlement;
import com.huskydreaming.settlements.persistence.roles.Role;
import com.huskydreaming.settlements.persistence.roles.RolePermission;
import com.huskydreaming.settlements.services.InventoryService;
import com.huskydreaming.settlements.services.SettlementService;
import com.huskydreaming.settlements.services.base.ServiceRegistry;
import com.huskydreaming.settlements.services.base.ServiceType;
import com.huskydreaming.settlements.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettlementInventory implements InventoryProvider {

    private final InventoryService inventoryService;

    private final Settlement settlement;

    public SettlementInventory(Settlement settlement) {
        this.settlement = settlement;
        inventoryService = (InventoryService) ServiceRegistry.getService(ServiceType.INVENTORY);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());
        contents.set(1, 1, citizensItem(player, settlement));
        contents.set(1, 2, roleItem(player, settlement));
        contents.set(1, 3, landItem(player, settlement));
        contents.set(1, 4, spawn(player, settlement));
        contents.set(1, 7, disband(player, settlement, contents));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem citizensItem(Player player, Settlement settlement) {
        Role role = settlement.getRole(player);
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Citizens")
                .setLore(ChatColor.GRAY + "Click to edit citizens.")
                .setMaterial(Material.EMERALD)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_CITIZENS) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e->
                inventoryService.getCitizensInventory(settlement).open(player)
        );
    }

    private ClickableItem roleItem(Player player, Settlement settlement) {
        Role role = settlement.getRole(player);
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Roles")
                .setLore(ChatColor.GRAY + "Click to edit roles.")
                .setMaterial(Material.WRITABLE_BOOK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_ROLES) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e->
                inventoryService.getRolesInventory(settlement).open(player)
        );
    }

    private ClickableItem landItem(Player player, Settlement settlement) {
        Role role = settlement.getRole(player);
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Lands")
                .setLore(ChatColor.GRAY + "Click to see lands.")
                .setMaterial(Material.GRASS_BLOCK)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_LAND) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e->
                inventoryService.getLandsInventory(settlement).open(player)
        );
    }

    private ClickableItem spawn(Player player, Settlement settlement) {
        Role role = settlement.getRole(player);
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.GREEN + "Spawn")
                .setLore(ChatColor.GRAY + "Click to set spawn.")
                .setMaterial(Material.ENDER_PEARL)
                .build();

        boolean permission = role.hasPermission(RolePermission.EDIT_SPAWN) || settlement.isOwner(player);
        return InventoryItem.of(permission, itemStack, e ->
                settlement.setLocation(player.getLocation())
        );
    }

    private ClickableItem disband(Player player, Settlement settlement, InventoryContents contents) {
        SettlementService settlementService = (SettlementService) ServiceRegistry.getService(ServiceType.SETTLEMENT);
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(ChatColor.RED + "Disband")
                .setLore(ChatColor.GRAY + "Click to disband settlement.")
                .setMaterial(Material.TNT_MINECART)
                .build();

        return InventoryItem.of(settlement.isOwner(player), itemStack, e -> {
            player.sendMessage("You have disbanded your settlement.");
            settlementService.disbandSettlement(settlement);
            contents.inventory().close(player);
        });
    }
}
